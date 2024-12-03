package main

import (
	"context"
	"log/slog"
	"net"
	"os/signal"
	"sync"
	"syscall"
	"tcp-client/internal"
)

func main() {
	ctx, stop := signal.NotifyContext(context.Background(), syscall.SIGTERM, syscall.SIGINT)
	defer stop()
	// TODO: DialTCPを使ってlocalhost:8080に接続する
	// そうすると、より詳細にFINとかを扱えるかも
	ltcpAddr := net.TCPAddr{
		IP:   net.ParseIP("localhost"),
		Port: 8080,
	}
	rtcpAddr := net.TCPAddr{
		IP:   net.ParseIP("localhost"),
		Port: 8090,
	}
	con, err := net.DialTCP("tcp", &ltcpAddr, &rtcpAddr)
	// TODO: bufferReadを使ってgraceful shutdownを試す
	err = con.SetReadBuffer(1)
	if err != nil {
		return
	}
	err = con.SetWriteBuffer(1)
	// closewriteはshtdownWRiteと同じ
	// 全二重送信のwrite側を閉じる
	// closeReadはshutdownReadと同じ
	// 全二重送信のread側を閉じる
	// なので、readしている方からプロセス切る場合は、まずcloseWriteを呼び出す
	// あ、そうするとclient側からレスポンスのwriteできないかのかな？？？　要確認
	// 参考:
	// https://ja.manpages.org/shutdown/2
	// https://ryuichi1208.hateblo.jp/entry/2021/12/19/204814
	//con.CloseWrite()
	//con.CloseWrite()

	// うーんなんかうまくいかないな。。。
	// ↓この辺の利用読むと手掛かりになるかも
	// https://github.com/golang/go/issues/67337

	// これも参考になるかも？
	// https://stackoverflow.com/questions/17803090/data-loss-even-if-remote-socket-is-gracefully-closed-in-java
	if err != nil {
		slog.Error("failed to dial: ", err)
		return
	}

	client := internal.NewTcpClient(con)
	slog.Info("dial success!")

	var wg sync.WaitGroup
	wg.Add(1)
	go func() {
		defer wg.Done()
		err := client.Read(ctx)
		if err != nil {
			slog.Error("failed to read: ", err)
			return
		}
	}()

	wg.Wait()
	client.Shutdown()
	slog.Info("finish process...")
}
