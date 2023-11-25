package main

import (
	"fmt"
	"strings"
)

func main() {
	//fmt.Println(RotLeft([]int32{1, 2, 3, 4, 5}, 12))
	fmt.Println(TextAnagram("cde", "abc"))
	fmt.Println(TextAnagram("eeeeeeeeeeeeeeeed", "eeeeeeeeeeeddd"))
	// -> eeeeeeeeeeedが残ればいいので、削除する文字列はeeeee5個とdd2個の7個

	fmt.Println(
		RansomeNote(
			strings.Split("ive got a lovely bunch of coconuts", " "),
			strings.Split("ive got a lovely bunch of coconuts  got lovely", " "),
		),
	)

}
