package main

import (
	"context"
	"fmt"
	"log/slog"
	"os/signal"
	"sync"
	"syscall"
	"tcp-server/internal"
	"time"
)

func main() {
	// コネクション取得
	con, err := internal.CreateTcpConnection()
	if err != nil {
		slog.Error(fmt.Errorf("failed to create tcp connection: %w", err).Error())
		return
	}

	// 5秒ずつ↑で取得したコネクションにデータを書き込む
	ctx, stop := signal.NotifyContext(context.Background(), syscall.SIGTERM, syscall.SIGINT)
	defer stop()

	slog.Info("start writing data to connection...")
	t := time.NewTicker(1 * time.Second)
	var wg sync.WaitGroup
	wg.Add(1)
	go func() {
		for {
			select {
			case <-ctx.Done():
				slog.Info("context done. ticker stopped.")
				wg.Done()
				return
			case <-t.C:
				slog.Info("write data to connection...")
				internal.WriteData(con)
			}
		}
	}()

	wg.Wait()
	slog.Info("context done. exiting...")
}
