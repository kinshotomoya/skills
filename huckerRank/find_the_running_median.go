package main

import "fmt"

func FindTheRunningMedian() {
	maxHeap := &MaxHeap{}
	maxHeap.add(1)
	maxHeap.add(3)
	maxHeap.add(5)
	maxHeap.add(6)
	maxHeap.add(2)
	maxHeap.add(17)
	maxHeap.add(8)
	maxHeap.add(98)
	maxHeap.add(4)
	maxHeap.poll()
	maxHeap.print()
}

type MaxHeap struct {
	heap []int
}

func (receiver *MaxHeap) add(element int) {
	receiver.heap = append(receiver.heap, element)
	addedIndex := len(receiver.heap) - 1
	for {
		if addedIndex == 0 || len(receiver.heap) == 1 {
			return
		} else {
			parentIndex := (addedIndex - 1) / 2
			if receiver.heap[parentIndex] < element {
				// 親よりも大きい場合、親と入れ替える
				receiver.heap[addedIndex] = receiver.heap[parentIndex]
				receiver.heap[parentIndex] = element
				addedIndex = parentIndex
			} else {
				// 親よりも小さい場合はその場所でおkなのでloop抜ける
				return
			}
		}
	}

}

// rootを取得して、ヒープ構造を再構築する
func (receiver *MaxHeap) poll() int {
	returnElement := receiver.heap[0]
	// 末尾を先頭に持ってくる
	receiver.heap[0] = receiver.heap[len(receiver.heap)-1]
	// 末尾を削除
	receiver.heap = receiver.heap[:len(receiver.heap)-1]
	targetIndex := 0
	for {
		if targetIndex >= len(receiver.heap)-1 {
			return returnElement
		}
		leftChildNodeIndex := 2*targetIndex + 1
		rightChildNodeIndex := 2*targetIndex + 2
		targetNode := receiver.heap[targetIndex]
		if leftChildNodeIndex <= len(receiver.heap)-1 && rightChildNodeIndex <= len(receiver.heap)-1 {
			// 左子供と右子供両方ある場合
			if receiver.heap[leftChildNodeIndex] > receiver.heap[rightChildNodeIndex] {
				if receiver.heap[leftChildNodeIndex] > receiver.heap[targetIndex] {
					receiver.heap[targetIndex] = receiver.heap[leftChildNodeIndex]
					receiver.heap[leftChildNodeIndex] = targetNode
					targetIndex = leftChildNodeIndex
				}
			} else if receiver.heap[rightChildNodeIndex] > receiver.heap[leftChildNodeIndex] {
				if receiver.heap[rightChildNodeIndex] > receiver.heap[targetIndex] {
					receiver.heap[targetIndex] = receiver.heap[rightChildNodeIndex]
					receiver.heap[rightChildNodeIndex] = targetNode
					targetIndex = rightChildNodeIndex
				}
			}
		} else if leftChildNodeIndex == len(receiver.heap)-1 {
			// 左子供だけある場合
			if receiver.heap[leftChildNodeIndex] > receiver.heap[targetIndex] {
				receiver.heap[targetIndex] = receiver.heap[leftChildNodeIndex]
				receiver.heap[leftChildNodeIndex] = targetNode
				targetIndex = leftChildNodeIndex
			} else {
				return returnElement
			}
		} else {
			// 子供がもうない場合
			return returnElement
		}
	}
}

// TODO: ヒープソートを適応
// これで配列の中央値を取得する！！
func (receiver *MaxHeap) sort() {

}

func (receiver *MaxHeap) print() {
	fmt.Println(receiver.heap)
}
