package internal

import (
	"context"
	"fmt"
	"log/slog"
	"net"
	"sync"
	"time"
)

type TcpClient struct {
	con      net.Conn
	wg       *sync.WaitGroup
	doneRead chan struct{}
}

func NewTcpClient(con net.Conn) *TcpClient {
	doneRead := make(chan struct{})
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
			c.Shutdown()
			return nil
		default:
			// loopで同じコネクションからデータを読み込み続ける
			// このサイズは適当
			// messageのサイズがわかっているなら、固定値を入れる（Visaからのサイズが決まった決済データであったり）
			b := make([]byte, 1024)
			// コネクションからデータを読み込む
			length, err := c.con.Read(b)
			slog.Info("read data: ", string(b[:length]))
			if err != nil {
				slog.Error(fmt.Errorf("failed to read: %w", err).Error())
				// TODO: 以下満たしたいがどうすればいいか
				// 1. con.Closeする前に、別サーバのTCPクライアントからのコネクションに対して新規データ書き込みを止める
				// 2. 新規でデータが書き込まれるのが止まった後に、現在コネクションに書き込まれているデータの読み込みが完了するまで待つ
				// 3. 読み込みが完了したら、connection.Closeする
				return err
			}
			c.wg.Add(1)
			go func() {
				defer c.wg.Done()
				time.Sleep(5 * time.Second)
				// 読み込んだデータをログに出力
				slog.Info("read data: in goroutine", string(b[:length]))
				c.con.Write([]byte(fmt.Sprintf("ack: %s", string(b[:length]))))
			}()
		}
	}
}

func (c *TcpClient) Shutdown() {
	// 他のサーバからのコネクションに対して新規データ書き込みを止めるためのシグナルをTCPサーバに送る
	c.con.Write([]byte("start shutdown"))
	slog.Info("context done. exiting...")
	// 今readしている内容が完了するまで待つ
	c.wg.Wait()

	c.con.Write([]byte("finish shutdown"))
	// コネクションをclient側から閉じる
	c.con.Close()
}
