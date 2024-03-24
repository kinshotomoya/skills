package internal

import (
	"bufio"
	"bytes"
	"encoding/binary"
	"fmt"
	"io"
	"os"
	"strings"
)

func BufferIo() {
	// 一定のサイズで指定したwriterを使って自動でflushしてくれる
	// 自前でbyte.bufferを使う必要はないってことか
	b := bufio.NewWriterSize(os.Stdout, 1024)
	b.WriteString("")

}

func MultiWriter() {

	f, _ := os.Create("hoge")
	// 指定したwriteにそれぞれ書き込める
	w := io.MultiWriter(os.Stdout, f)
	w.Write([]byte("hoge"))

	r := strings.NewReader("hoge")
	io.NopCloser(r)

	// ビッグエンディアンでデータを持っている
	// 10000
	b := []byte{0x00, 0x00, 0x27, 0x10}
	var i int32
	// bytes.NewReader(b)からiにデータを読み込む
	// 読み込む際に読み込むデータはビッグエンディアンだと指定している
	// x86_64はリトルエンディアンなので、↑指定することでビッグエンディアン -> リトルエンディアン変換をしている
	binary.Read(bytes.NewReader(b), binary.BigEndian, &i)
	fmt.Printf("%d\n", i)

	//os.Create()

}
