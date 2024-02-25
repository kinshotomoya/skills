package internal

import (
	"fmt"
	"runtime"
)

func SelectGoroutine() {

	// 複数のchannelをselectで待ち受けしていて同時に解放したらどうなるのか？
	c1 := make(chan any)
	close(c1)
	c2 := make(chan any)
	close(c2)

	var c1Count, c2Count int
	for i := 1000; i >= 0; i-- {
		select {
		case <-c1:
			c1Count++
		case <-c2:
			c2Count++
		}
	}

	fmt.Printf("c1Count: %d, c2Count: %d", c1Count, c2Count)

	// どちらも平均的に実行される
	// これはgoの仕様
	// -> c1Count: 492, c2Count: 509

	//var sendOnlyChan chan<- int
	//var receiveOnlyChan <-chan int

	fmt.Println(runtime.NumCPU())

}
