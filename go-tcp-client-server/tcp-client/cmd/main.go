package main

import (
	"context"
	"log/slog"
	"net"
	"os"
	"os/signal"
	"sync"

	"syscall"
	"tcp-client/internal"
)

func main() {
	ctx, stop := signal.NotifyContext(context.Background(), syscall.SIGTERM, syscall.SIGINT)
	defer stop()

	// 自分でsocketを作って、それを使ってTCP通信をする
	// linuxでは、SO_RCVBUFの設定はconnectionする前に設定しないといけない
	// 参考：https://groups.google.com/g/golang-nuts/c/0FQ8duKRB4U/m/QK_mo6nEV9wJ

	// NOTE:
	// window sizeとは、受信側が受け取れるデータの量のこと
	// これを設定することで、受信側が受け取れるデータの量を制限できる
	// 送信側はこのwindow sizeを超えるまではackが返ってくる前にデータを送信できる
	// window sizeを超えた場合、全ての送信済みパケットに対してのackが返ってくるまで送信できない
	// 参考：https://milestone-of-se.nesuke.com/nw-basic/tcp-udp/window-size-mss-mtu/
	socket, err := syscall.Socket(syscall.AF_INET, syscall.SOCK_STREAM, 0)
	if err != nil {
		slog.Error("syscall.Socket: ", err)
		return
	}
	//getsockoptByte, err := syscall.GetsockoptByte(socket, syscall.SOL_SOCKET, syscall.SO_RCVBUF)
	//if err != nil {
	//	return
	//}
	//slog.Info("before getsockoptByte: ", getsockoptByte)

	// これで受信機側のwindow sizeを設定できると思ったが...
	// wireshrkでみてもWindow size value: 6379
	// Window size value: 510      ←これがwindow sizeの値（受信機側で設定されている値）
	// [Calculated window size: 4080]  ←これがwindow size * scaling factorの値（実際に送信側が遅れるサイズ）
	// [Window size scaling factor: 8]    ←係数、これによってwindow sizeが8倍される
	err = syscall.SetsockoptInt(socket, syscall.SOL_SOCKET, syscall.SO_RCVBUF, 1024)
	if err != nil {
		slog.Error("syscall.SetsockoptInt: ", err)
		return
	}

	// ↓これは、送信側のwindow sizeを設定する
	//err = syscall.SetsockoptInt(socket, syscall.SOL_SOCKET, syscall.SO_SNDBUF, 1024)
	//if err != nil {
	//	slog.Error("syscall.SetsockoptInt: ", err)
	//	return
	//}

	//getsockoptByte, err = syscall.GetsockoptByte(socket, syscall.SOL_SOCKET, syscall.SO_RCVBUF)
	//if err != nil {
	//	return
	//}
	//slog.Info("after getsockoptByte: ", getsockoptByte)
	err = syscall.Connect(socket, &syscall.SockaddrInet4{
		Port: 8090,
		Addr: [4]byte{127, 0, 0, 1},
	})
	if err != nil {
		slog.Error("syscall.Connect: ", err)
		return
	}
	f := os.NewFile(uintptr(socket), "tcp")
	con, err := net.FileConn(f)
	if err != nil {
		return
	}
	// そうすると、より詳細にFINとかを扱えるかも
	//ltcpAddr := net.TCPAddr{
	//	IP:   net.ParseIP("localhost"),
	//	Port: 8080,
	//}
	//rtcpAddr := net.TCPAddr{
	//	IP:   net.ParseIP("localhost"),
	//	Port: 8090,
	//}
	//con, err := net.DialTCP("tcp", &ltcpAddr, &rtcpAddr)
	// TODO: bufferReadを使ってgraceful shutdownを試す
	//err = con.SetReadBuffer(10)
	//if err != nil {
	//	return
	//}
	//err = con.SetWriteBuffer(1)
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
