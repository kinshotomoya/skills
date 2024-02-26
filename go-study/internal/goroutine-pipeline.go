package internal

import (
	"fmt"
	"sync"
)

func Pipeline() {

	generator := func(done <-chan any, integers ...int) <-chan int {
		intStream := make(chan int, len(integers))
		go func() {
			defer close(intStream)
			for _, v := range integers {
				select {
				case <-done:
					return
				case intStream <- v:
				}
			}
		}()

		return intStream
	}

	multiply := func(done <-chan any, intStream <-chan int, multiplier int) <-chan int {
		muliStream := make(chan int)
		go func() {
			defer close(muliStream)
			for i := range intStream {
				select {
				case <-done:
					return
				case muliStream <- i * multiplier:
				}
			}
		}()
		return muliStream
	}

	add := func(done <-chan any, intStream <-chan int, addtitive int) <-chan int {
		addStream := make(chan int)

		go func() {
			defer close(addStream)
			for i := range intStream {
				select {
				case <-done:
					return
				case addStream <- i + addtitive:
				}
			}
		}()

		return addStream
	}

	done := make(chan any)
	defer close(done)

	intStream := generator(done, 1, 2, 3, 4)
	pipleline := multiply(done, add(done, multiply(done, intStream, 2), 1), 2)

	for v := range pipleline {
		fmt.Println(v)
	}

}

func RepeatAndTake() {
	repeat := func(done <-chan any, values ...any) <-chan any {
		repeatStream := make(chan any)

		go func() {
			defer close(repeatStream)
			for {
				for _, v := range values {
					select {
					case <-done:
					case repeatStream <- v:
					}
				}
			}

		}()
		return repeatStream
	}

	take := func(done <-chan any, repeatStream <-chan any, takeNum int) <-chan any {
		takeStream := make(chan any)
		go func() {
			defer close(takeStream)
			for i := 0; i < takeNum; i++ {
				select {
				case <-done:
					return
				case takeStream <- <-repeatStream:
				}
			}
		}()
		return takeStream
	}

	done := make(chan any)
	defer close(done)
	values := take(done, repeat(done, 1, 2, 3), 10000)
	for v := range values {
		fmt.Printf("%v", v)
	}

}

func FunInAndFunOut() {
	funIn := func(done <-chan any, channels ...<-chan any) <-chan any {
		// 複数のストリームを束ねるストリーム
		multiplexStream := make(chan any)
		var wg sync.WaitGroup

		multiplex := func(channel <-chan any) {
			defer wg.Done()
			for v := range channel {
				select {
				case <-done:
					return
				case multiplexStream <- v:
				}
			}
		}

		for _, c := range channels {
			wg.Add(1)
			go multiplex(c)
		}

		go func() {
			wg.Wait()
			// 全てのストリームの読み込むが終わったら、multiplexStreamチャネルを閉じる
			// チャネルを閉じる責務はチャネルを作ったスコープで行うべき！
			close(multiplexStream)
		}()

		return multiplexStream
	}

}
