package main

import (
	"context"
	"log/slog"
	"net"
	"os/signal"
	"sync"
	"syscall"
	"tcp-client/internal"
	"time"
)

func main() {
	ctx, stop := signal.NotifyContext(context.Background(), syscall.SIGTERM, syscall.SIGINT)
	defer stop()
	con, err := net.DialTimeout("tcp", "localhost:8080", 10*time.Second)
	if err != nil {
		slog.Error("failed to dial: ", err)
		return
	}
	defer con.Close()

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
	slog.Info("context done. exiting...")

}
