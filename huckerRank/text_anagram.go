package main

import (
	"fmt"
	"math"
	"strings"
)

// cde, ffe
// -> eだけ残るので、4文字削除する
// fcrxzwscanmligyxyvym, jxwtrhvujlmrpdoqbisbwhmgpmeoke
func TextAnagram(a string, b string) int32 {
	container := make(map[string]int)
	// accfgilmmnrsvwxxyyyz
	// bbdeeghhijjklmmmooppqrrstuvwwx

	// accbbdeef
	arrayA := strings.Split(a, "")
	arrayB := strings.Split(b, "")

	for i := range arrayA {
		container[arrayA[i]]++
	}
	fmt.Println(container)

	for i := range arrayB {
		_, exist := container[arrayB[i]]
		if exist {
			container[arrayB[i]]--
		} else {
			container[arrayB[i]+"_b"]++
		}
	}
	var result float64
	for i := range container {
		result += math.Abs(float64(container[i]))
	}
	//return len(arrayA) + len(arrayB) - counter*2
	return int32(result)

}
