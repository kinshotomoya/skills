package main

import (
	"fmt"
	"sync"
	"time"
)

// SyncCond ストリームデータを受け取って、queueに10ずつ増えると複数のgoroutineが起動しqueueから値を取り出す例
func SyncCond() {
	fetchSize := 2 // queueから取り出すサイズ
	// クリティカルセクション（複数goroutineから読み書きされるメモリ）
	queue := make([]int, 0, 10)

	sender := func(c *sync.Cond, name string, fn func()) {
		var runningGoroutineWg sync.WaitGroup
		runningGoroutineWg.Add(1)
		go func() {
			runningGoroutineWg.Done()
			defer fn()
			for {
				c.L.Lock()                                                         // クリティカルセクションをロックする
				c.Wait()                                                           // シグナル受け取るまで待機する。waitが走った時点で、一旦↑のlockは解除される
				fmt.Printf("%s fetch queue content %d\n", name, queue[:fetchSize]) // クリティカルセクションを読み込む
				queue = queue[fetchSize:]                                          // クリティカルセクションにライトする（queueから取り出した分を削除する）
				c.L.Unlock()                                                       // 最後にクリティカルセクションをアンロックする
			}
		}()
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

	// ストリームデータを受け取るイメージ
	for i := 0; i < 1000; i++ {
		time.Sleep(1 * time.Second)
		c.L.Lock()
		queue = append(queue, i)
		if len(queue)%10 == 0 {
			c.Broadcast()
		}
		c.L.Unlock()
	}

	wg.Wait()

}
