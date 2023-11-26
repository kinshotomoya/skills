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

	// 問題: https://www.hackerrank.com/challenges/ctci-balanced-brackets/problem?h_r=internal-search
	fmt.Println(IsBalanced("{{[[(())]))]}}"))

	// 問題:https://www.hackerrank.com/challenges/ctci-queue-using-two-stacks/problem?h_r=internal-search
	ATableOfTwoStacks()

	// 問題：https://www.hackerrank.com/challenges/tree-inorder-traversal/problem?h_r=internal-search
	binaryNode7 := &BinaryNode{value: 7}
	binaryNode9 := &BinaryNode{value: 9}
	binaryNode16 := &BinaryNode{value: 16}
	binaryNode8 := &BinaryNode{value: 8, right: binaryNode9, left: binaryNode7}
	binaryNode15 := &BinaryNode{value: 15, right: binaryNode16, left: binaryNode8}
	binaryNode6 := &BinaryNode{value: 6, right: binaryNode15}
	binaryNode4 := &BinaryNode{value: 4}
	binaryNode3 := &BinaryNode{value: 3, right: binaryNode4}
	binaryNode5 := &BinaryNode{value: 5, right: binaryNode6, left: binaryNode3}
	binaryNode2 := &BinaryNode{value: 2, right: binaryNode5}
	binaryNode1 := &BinaryNode{value: 1, right: binaryNode2}
	InorderTraversal(binaryNode1)

}
