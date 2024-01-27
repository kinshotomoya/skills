package main

import (
	"fmt"
	"sync"
)

func SyncCond() {
	// クリティカルセクション（複数goroutineから読み書きされるメモリ）
	queue := make([]int, 0, 10)

	sender := func(c *sync.Cond, name string, fn func()) {
		var wg sync.WaitGroup
		wg.Add(1)
		go func() {
			wg.Done()
			c.L.Lock()         // クリティカルセクションをロックする
			defer c.L.Unlock() // 最後にクリティカルセクションをアンロックする
			c.Wait()           // シグナル受け取るまで待機する
			// TODO: 10個しか表示しないように
			fmt.Printf("%s send %d\n", name, queue) // クリティカルセクションを読み込む
			fn()
		}()
		wg.Wait()
	}

	var wg sync.WaitGroup
	wg.Add(2)
	c := sync.NewCond(&sync.Mutex{})
	sender(c, "sender-1", func() {
		wg.Done()
	})
	sender(c, "sender-2", func() {
		wg.Done()
	})

	for i := 0; i < 100000; i++ {
		c.L.Lock()
		queue = append(queue, i)
		if len(queue) == 10 {
			c.Broadcast()
		}
		c.L.Unlock()
	}
	wg.Wait()

}
