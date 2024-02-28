package internal

func GoroutineTee() {

	tee := func(done <-chan any, in <-chan any) (_, _ <-chan any) {
		out1 := make(chan any)
		out2 := make(chan any)
		go func() {
			defer close(out1)
			defer close(out2)
			for v := range in {
				var out1, out2 = out1, out2
				for i := 0; i < 2; i++ {
					select {
					case <-done:
						return
					case out1 <- v:
						// 書き込みが終了するとout1にnilを代入して（chanじゃなくすことで次のloopで書き込めなくする）それ以降の書き込みをブロックする
						out1 = nil
					case out2 <- v:
						// 書き込みが終了するとout2にnilを代入して（chanじゃなくすことで次のloopで書き込めなくする）それ以降の書き込みをブロックする
						out2 = nil
					}
				}
			}
		}()
		return out1, out2
	}

}
