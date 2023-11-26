package main

import "fmt"

// TODO:https://www.hackerrank.com/challenges/tree-inorder-traversal/problem?h_r=internal-search
func InorderTraversal(root *BinaryNode) {
	// for文で書く
	// 左のノードを操作する際のターニングポイントをスタックしておく
	stack := make([]*BinaryNode, 0)
	for {
		if root == nil && len(stack) == 0 {
			return
		}
		if root == nil && len(stack) != 0 {
			root = stack[len(stack)-1]
			stack = stack[:len(stack)-1]
			root.print()
			root = root.right
			continue
		}
		if root.left == nil {
			root.print()
			root = root.right
			continue
		} else {
			stack = append(stack, root)
			root = root.left
			continue
		}
	}

}

type BinaryNode struct {
	value int
	right *BinaryNode
	left  *BinaryNode
}

func (receiver *BinaryNode) print() {
	fmt.Println(receiver.value)
}
