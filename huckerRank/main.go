package main

import (
	"fmt"
	"strings"
)

func main() {
	// 問題: https://www.hackerrank.com/challenges/ctci-array-left-rotation/forum
	fmt.Println(RotLeft([]int32{1, 2, 3, 4, 5}, 12))

	// 問題: https://www.hackerrank.com/challenges/ctci-making-anagrams/problem?h_r=internal-search
	fmt.Println(TextAnagram("cde", "abc"))
	fmt.Println(TextAnagram("eeeeeeeeeeeeeeeed", "eeeeeeeeeeeddd"))
	// -> eeeeeeeeeeedが残ればいいので、削除する文字列はeeeee5個とdd2個の7個

	// 問題: https://www.hackerrank.com/challenges/ctci-ransom-note/problem?h_r=internal-search
	fmt.Println(
		RansomeNote(
			strings.Split("ive got a lovely bunch of coconuts", " "),
			strings.Split("ive got a lovely bunch of coconuts  got lovely", " "),
		),
	)

	// 問題:https://www.hackerrank.com/challenges/ctci-linked-list-cycle/problem?h_r=internal-search
	paths := make(map[*Node]struct{})
	node5 := &Node{next: nil}
	node4 := &Node{next: node5}
	node3 := &Node{next: node4}
	node2 := &Node{next: node3}
	node1 := &Node{next: node2}
	node5.next = node3
	fmt.Println(DetectCycle(node1, paths))
}
