package main

import (
	"fmt"
	"sync"
)

func SyncPool() {

	var numCalcsCreated int
	calcPool := &sync.Pool{
		New: func() any {
			numCalcsCreated += 1
			mem := make([]byte, 1024) // 1kbのbyte配列を作成
			return &mem
		},
	}

	// 4kbのbyte配列を作成する
	calcPool.Put(calcPool.New)
	calcPool.Put(calcPool.New)
	calcPool.Put(calcPool.New)
	calcPool.Put(calcPool.New)

	const numWorkers = 1024 * 1024

	var wg sync.WaitGroup
	wg.Add(numWorkers)
	for i := numWorkers; i > 0; i-- {
		go func() {
			defer wg.Done()
			mem := calcPool.Get()   // poolにあればpoolから取り出す、なければNew関数が呼ばれて作成される
			defer calcPool.Put(mem) // poolに戻す

			// 何か処理

		}()
	}

	wg.Wait()
	fmt.Printf("%d calculators crated", numCalcsCreated)

}
