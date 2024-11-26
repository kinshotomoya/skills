package internal

import (
	"log/slog"
	"net"
)

func CreateTcpConnection() (*net.TCPConn, error) {
	addr := net.TCPAddr{
		IP:   net.ParseIP("localhost"),
		Port: 8090,
	}
	listen, err := net.ListenTCP("tcp", &addr)
	if err != nil {
		return nil, err
	}
	slog.Info("waiting for connection...")
	con, err := listen.AcceptTCP()
	slog.Info("connected!")
	if err != nil {
		return nil, err
	}

	return con, nil
}

func WriteData(con net.Conn, data []byte) error {
	_, err := con.Write(data)
	if err != nil {
		return err
	}
	return nil
}
