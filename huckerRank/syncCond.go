package main

import (
	"fmt"
	"sync"
)

func SyncCond() {
	// クリティカルセクション（複数goroutineから読み書きされるメモリ）
	queue := make([]int, 0, 10)
	var checkpoint int

	sender := func(c *sync.Cond, name string, fn func()) {
		var runningGoroutineWg sync.WaitGroup
		runningGoroutineWg.Add(1)
		go func() {
			runningGoroutineWg.Done()
			for {
				c.L.Lock() // クリティカルセクションをロックする
				c.Wait()   // シグナル受け取るまで待機する
				// TODO: checkpointを用いて、前回からの続きの10個しか表示しないように
				fmt.Printf("%s send %d\n", name, queue[checkpoint:10]) // クリティカルセクションを読み込む
				checkpoint = 1
				c.L.Unlock() // 最後にクリティカルセクションをアンロックする
			}
		}()
		fn()
		runningGoroutineWg.Wait()
	}

	var wg sync.WaitGroup
	c := sync.NewCond(&sync.Mutex{})
	wg.Add(2)
	sender(c, "sender-1", func() {
		wg.Done()
	})
	sender(c, "sender-2", func() {
		wg.Done()
	})

	for i := 0; i < 1000; i++ {
		c.L.Lock()
		queue = append(queue, i)
		if len(queue)%9 == 0 {
			c.Broadcast()
		}
		c.L.Unlock()
	}
	wg.Wait()

}
