package main

import (
	"fmt"
	"math/rand"
)

func pattern1() {

	// アドホック拘束
	// ルールによって変数のスコープを拘束するパターン
	// すぐに破られるのでやりたくない

	// このsliceはloopData関数の中でも、dataStreamのfor rangeの中でもどちらでも使えるが
	// ルールによって、loopData関数の中でしか利用しないようになっている
	// これがアドホック拘束
	data := []int{1, 2, 3, 4, 5}

	// 送信専用channelとして受け取る
	loopData := func(dataStream chan<- int) {
		defer close(dataStream)
		for i := range data {
			dataStream <- i
		}
	}

	dataStream := make(chan int)
	go loopData(dataStream)

	for i := range dataStream {
		fmt.Println(i)
	}

}

func pattern2() {
	// レキシカルスコープ（静的スコープ）
	// コードを書いた時点で変数のスコープが決まっている

	// このようにすることで、results channelは直接利用できなくなる

	// 読み込み専用channelを返す
	chanOwner := func() <-chan int {
		// results channelへの書き込み権限をこのスコープ内に収めている
		results := make(chan int, 5)
		go func() {
			defer close(results)
			for i := 0; i <= 5; i++ {
				results <- i
			}
		}()

		return results
	}

	consumer := func(results <-chan int) {
		for re := range results {
			fmt.Println(re)
		}
		fmt.Println("Done")
	}

	c := chanOwner()
	consumer(c)

}

// これはmain goroutineからnewRandStream goroutine側に終了通知を行っていないのでnewRandStream goroutineが正常に終了していない
func pattern3() {
	newRandStream := func() <-chan int {
		randStream := make(chan int)
		go func() {
			defer fmt.Println("newRandStream closure exists.")
			defer close(randStream)

			for {
				// デフォルトのバッファサイズは0
				// なので読み込まないとブロックする
				randStream <- rand.Int()
			}

		}()

		return randStream
	}

	randStream := newRandStream()
	for i := 1; i <= 3; i++ {
		fmt.Printf("%d: %d\n", i, <-randStream)
	}

}
