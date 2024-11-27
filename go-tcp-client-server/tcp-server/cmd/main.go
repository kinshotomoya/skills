package main

import (
	"context"
	"errors"
	"fmt"
	"io"
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

	ctx, stop := signal.NotifyContext(context.Background(), syscall.SIGTERM, syscall.SIGINT)
	defer stop()

	// コネクションに対してデータを読み込む
	readData := make(chan struct{})
	var wg1 sync.WaitGroup
	wg1.Add(1)
	go func() {
		defer wg1.Done()
		b := make([]byte, 1024)
		for {
			select {
			case <-ctx.Done():
				slog.Info("context done. stop reading data.")
				return
			default:
				_, err := con.Read(b)
				if err != nil {
					if errors.Is(err, io.EOF) {
						slog.Info("connection closed from TCP Client.")
						readData <- struct{}{}
						return
					}
				}
			}
		}
	}()

	slog.Info("start writing data to connection...")
	t := time.NewTicker(1 * time.Second)
	var wg sync.WaitGroup
	wg.Add(1)
	go func() {
		defer wg.Done()
		var counter int
		for {
			select {
			case <-ctx.Done():
				slog.Info("context done. ticker stopped.")
				return
			case <-readData:
				slog.Info("stop writing...")
				return
			case <-t.C:
				slog.Info(fmt.Sprintf("writing data %d...", counter))
				err := internal.WriteData(con, []byte(fmt.Sprintf("data %d\n", counter)))
				if err != nil {
					slog.Error(fmt.Errorf("failed to write data: %w", err).Error())
					return
				}
				counter++
			}
		}
	}()

	wg.Wait()
	wg1.Wait()
	slog.Info("context done. exiting...")
}
