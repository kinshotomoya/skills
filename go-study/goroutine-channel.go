package main

import "fmt"

func receiveChannel() {

	// channelのスコープはできる限り小さくすることでどのように振る舞うのかが明瞭になる
	chaOwner := func() <-chan int {
		resultStream := make(chan int, 5)

		go func() {
			defer close(resultStream)
			for i := 0; i <= 5; i++ {
				resultStream <- i
			}
		}()

		return resultStream
	}

	for result := range chaOwner() {
		fmt.Println(result)
	}

	fmt.Println("Done")

}
