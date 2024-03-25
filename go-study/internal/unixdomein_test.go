package internal

import (
	"bufio"
	"net"
	"net/http"
	"net/http/httputil"
	"os"
	"path/filepath"
	"testing"
	"time"
)

func BenchmarkTCPServer(b *testing.B) {
	for i := 0; i < b.N; i++ {
		conn, err := net.Dial("tcp", "localhost:18888")
		if err != nil {
			return
		}

		request, err := http.NewRequest("get", "http://localhost:18888", nil)
		if err != nil {
			return
		}

		request.Write(conn)

		response, err := http.ReadResponse(bufio.NewReader(conn), request)
		if err != nil {
			return
		}

		_, err = httputil.DumpResponse(response, true)
		if err != nil {
			return
		}

	}
}

func BenchmarkUnixDomainSocketServer(b *testing.B) {
	for i := 0; i < b.N; i++ {
		conn, err := net.Dial("unix", filepath.Join(os.TempDir(), "bench-unixdomainsocket"))
		if err != nil {
			return
		}

		request, err := http.NewRequest("get", "http://localhost:18888", nil)
		if err != nil {
			return
		}

		request.Write(conn)

		response, err := http.ReadResponse(bufio.NewReader(conn), request)
		if err != nil {
			return
		}

		_, err = httputil.DumpResponse(response, true)
		if err != nil {
			return
		}

	}
}

func TestMain(m *testing.M) {

	go UnixDomainSocketServer()
	go TCPServer()

	time.Sleep(time.Second)

	code := m.Run()

	os.Exit(code)

}

// l

//[git][* master]:~/work_space/skills/go-study/internal/ go test -bench . -benchmem                          [/Users/jinzhengpengye/work_space/skills/go-study/internal]
//goos: darwin
//goarch: amd64
//pkg: go-study/internal
//cpu: Intel(R) Core(TM) i7-8569U CPU @ 2.80GHz
//BenchmarkTCPServer-8                        2425            493978 ns/op           52889 B/op        113 allocs/op
//BenchmarkUnixDomainSocketServer-8          11878            103147 ns/op           51513 B/op         76 allocs/op
//PASS
//ok      go-study/internal       7.768s
