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
				switch {
				case errors.Is(err, io.EOF):
					slog.Error("connection closed from TCP Server.")
					return nil
				default:
					slog.Error(fmt.Errorf("failed to read: %w", err).Error())
					continue
				}
			}
			c.wg.Add(1)
			c.counter++
			go func() {
				defer c.wg.Done()
				time.Sleep(5 * time.Second)
				// ここでackを返す
				c.con.Write([]byte(fmt.Sprintf("ack: %s", string(b[:length]))))
			}()
		}
	}
}

func (c *TcpClient) Shutdown() {
	// 他のサーバからのコネクションに対して新規データ書き込みを止めるためのシグナルをTCPサーバに送る
	//c.con.Write([]byte("start shutdown"))
	slog.Info("context done. exiting...")

	err := c.con.CloseWrite()
	if err != nil {
		slog.Info(fmt.Errorf("failed to close write: %w", err).Error())
	}
	// 今readしている内容が完了するまで待つ
	c.wg.Wait()
	time.Sleep(30 * time.Second)
	//_, err := c.con.Write([]byte("finish shutdown"))
	//if err != nil {
	//	slog.Error(fmt.Errorf("failed to write: %w", err).Error())
	//}

	// コネクションをclient側から閉じる
	// 中身見るとfdを破棄している
	c.con.Close()
	slog.Info(fmt.Sprintf("counter: %d", c.counter))
}
