package main

import "fmt"

func main() {
	//fmt.Println(RotLeft([]int32{1, 2, 3, 4, 5}, 12))
	fmt.Println(TextAnagram("cde", "abc"))
	fmt.Println(TextAnagram("eeeeeeeeeeeeeeeed", "eeeeeeeeeeeddd"))
	// -> eeeeeeeeeeedが残ればいいので、削除する文字列はeeeee5個とdd2個の7個

}
