package internal

import (
	"fmt"
	"log/slog"
	"net"
)

func CreateTcpConnection() (net.Conn, error) {
	listen, err := net.Listen("tcp", ":8080")
	if err != nil {
		return nil, err
	}
	slog.Info("waiting for connection...")
	con, err := listen.Accept()
	slog.Info("connected!")
	if err != nil {
		return nil, err
	}

	return con, nil
}

func WriteData(con net.Conn) {
	request := []byte("Hello, how have you been?")
	_, err := con.Write(request)
	if err != nil {
		slog.Error(fmt.Errorf("failed to write: %w", err).Error())
		return
	}
}
