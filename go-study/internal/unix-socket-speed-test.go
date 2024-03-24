package internal

import (
	"bufio"
	"io"
	"net"
	"net/http"
	"net/http/httputil"
	"os"
	"path/filepath"
	"strings"
)

func TCPServer() {

	listen, err := net.Listen("tcp", "localhost:18888")
	if err != nil {
		return
	}

	for {
		conn, err := listen.Accept()
		if err != nil {
			return
		}

		go func() {

			request, err := http.ReadRequest(bufio.NewReader(conn))
			if err != nil {
				return
			}

			_, err = httputil.DumpRequest(request, true)
			if err != nil {
				return
			}

			response := http.Response{
				Status:     "200",
				ProtoMajor: 1,
				ProtoMinor: 0,
				Body:       io.NopCloser(strings.NewReader("hello world\n")),
			}

			response.Write(conn)
			conn.Close()

		}()
	}

}

func UnixDomainSocketServer() {

	path := filepath.Join(os.TempDir(), "bench-unixdomainsocket")
	os.Remove(path)
	listen, err := net.Listen("unix", path)
	if err != nil {
		return
	}

	for {
		conn, err := listen.Accept()
		if err != nil {
			return
		}

		go func() {

			request, err := http.ReadRequest(bufio.NewReader(conn))
			if err != nil {
				return
			}

			_, err = httputil.DumpRequest(request, true)
			if err != nil {
				return
			}

			response := http.Response{
				Status:     "200",
				ProtoMajor: 1,
				ProtoMinor: 0,
				Body:       io.NopCloser(strings.NewReader("hello world\n")),
			}

			response.Write(conn)
			conn.Close()

		}()
	}

}
