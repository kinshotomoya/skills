package internal

import (
	"context"
	"errors"
	"fmt"
	"io"
	"log/slog"
	"net"
	"sync"
	"time"
)

type TcpClient struct {
	con      *net.TCPConn
	wg       *sync.WaitGroup
	doneRead chan struct{}
	counter  int
}

func NewTcpClient(con *net.TCPConn) *TcpClient {
	doneRead := make(chan struct{})
	//con.SetReadDeadline(time.Now().Add(5 * time.Second))
	var wg sync.WaitGroup
	return &TcpClient{
		con:      con,
		wg:       &wg,
		doneRead: doneRead,
	}
}

func (c *TcpClient) Read(ctx context.Context) error {
	for {
		select {
		case <-ctx.Done():
			// 書き込み側をcloseする
			// これはclient側からのwriteをできなくするだけで、serverからのwriteはできる
			// これを受け取ったserver側はreadした際にio.EOFを返す
			// なので、client側からのwriteをできなくするためには、server側からのwriteを止める必要がある
			// CloseWrite()をすると、こちら側からserverに対してFinを送る
			//c.con.CloseWrite()

			// 読み込み側をcloseする
			// これはclient側からのreadをできなくするだけで、serverはclientからのreadをできる
			// これを受け取ったserver側はwriteした際にio.EOFを返す
			// closeReadした時点で書き込まれているデータはlostしてしまう...
			// CloseRead()をすると、こちら側からserverに対してRSTを送る
			c.con.CloseRead()

			// NOTE: application levelでの解決をするしかないっぽい
			//  https://stackoverflow.com/questions/79230835/gracefully-closing-a-tcp-connection-from-client-side-in-go-while-ensuring-all-da?noredirect=1#comment139715989_79230835
			slog.Info("stop writing...")
			//_, err := c.con.Write([]byte("hogeeee"))
			//if err != nil {
			//	slog.Error(fmt.Errorf("failed to write: %w", err).Error())
			//}
			for {
				_, err := c.read()
				if err != nil {
					if errors.Is(err, io.EOF) {
						slog.Error("connection closed from TCP Server.")
						return nil
					}
					return err
				}
			}
		default:
			// コネクションからデータを読み込む
			_, err := c.read()
			time.Sleep(200 * time.Microsecond)
			if err != nil {
				switch {
				case errors.Is(err, io.EOF):
					slog.Error("connection closed from TCP Server.")
					return nil
				default:
					slog.Error(fmt.Errorf("failed to read: %w", err).Error())
					continue
				}
			}
			//c.wg.Add(1)
			//c.counter++
			//go func() {
			//	defer c.wg.Done()
			//	time.Sleep(5 * time.Second)
			//	// ここでackを返す
			//	c.con.Write([]byte(fmt.Sprintf("ack: %s", string(b[:length]))))
			//}()
		}
	}
}

func (c *TcpClient) read() (int, error) {
	// loopで同じコネクションからデータを読み込み続ける
	// このサイズは適当
	// messageのサイズがわかっているなら、固定値を入れる（Visaからのサイズが決まった決済データであったり）
	b := make([]byte, 10)
	length, err := c.con.Read(b)
	if err != nil {
		return 0, err
	}
	slog.Info(fmt.Sprintf("read data: %s", string(b[:length])))
	return length, nil
}

func (c *TcpClient) Shutdown() {
	// 他のサーバからのコネクションに対して新規データ書き込みを止めるためのシグナルをTCPサーバに送る
	//c.con.Write([]byte("start shutdown"))
	slog.Info("context done. exiting...")

	err := c.con.CloseRead()
	if err != nil {
		slog.Info(fmt.Errorf("failed to close write: %w", err).Error())
	}
	// 今readしている内容が完了するまで待つ
	c.wg.Wait()
	time.Sleep(10 * time.Second)
	//_, err := c.con.Write([]byte("finish shutdown"))
	//if err != nil {
	//	slog.Error(fmt.Errorf("failed to write: %w", err).Error())
	//}

	// コネクションをclient側から閉じる
	// 中身見るとfdを破棄している
	c.con.Close()
	slog.Info(fmt.Sprintf("counter: %d", c.counter))
}
