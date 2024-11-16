package internal

import (
	"context"
	"fmt"
	"io"
	"log/slog"
	"net"
	"time"
)

type TcpClient struct {
	con      net.Conn
	doneRead chan struct{}
}

func NewTcpClient(con net.Conn) *TcpClient {
	doneRead := make(chan struct{})
	return &TcpClient{
		con:      con,
		doneRead: doneRead,
	}
}

func (c *TcpClient) Read(ctx context.Context) error {
	for {
		select {
		case <-ctx.Done():
			slog.Info("context done. exiting...")
			// 今readしている内容が完了するまで待つ
			<-c.doneRead
			return nil
		default:
			// loopで同じコネクションからデータを読み込み続ける
			// このサイズは適当
			// messageのサイズがわかっているなら、固定値を入れる（Visaからのサイズが決まった決済データであったり）
			b := make([]byte, 1024)
			// コネクションからデータを読み込む
			_, err := c.con.Read(b)
			time.Sleep(10 * time.Second)
			if err != nil {
				slog.Error(fmt.Errorf("failed to read: %w", err).Error())
				// TODO: 以下満たしたいがどうすればいいか
				// 1. con.Closeする前に、別サーバのTCPクライアントからのコネクションに対して新規データ書き込みを止める
				// 2. 新規でデータが書き込まれるのが止まった後に、現在コネクションに書き込まれているデータの読み込みが完了するまで待つ
				// 3. 読み込みが完了したら、connection.Closeする
				if err == io.EOF {
					c.doneRead <- struct{}{}
					return nil
				}
				return err
			}
			if err != nil {
				slog.Error("failed to read data")
				return err
			}
			// 読み込んだデータをログに出力
			slog.Info("read data: ", string(b))
		}
	}
}
