package main

import "fmt"

// TODO: https://www.hackerrank.com/challenges/ctci-queue-using-two-stacks/problem?h_r=internal-search
func ATableOfTwoStacks() {
	// type:
	//  1: enqueue
	//	2:dequeue
	//	3: print head of queue

	qeueue := Queue{}

	qeueue.enqueue(42)
	qeueue.dequeue()
	qeueue.enqueue(14)
	qeueue.print()
	qeueue.enqueue(28)
	qeueue.print()
	qeueue.enqueue(60)
	qeueue.enqueue(78)
	qeueue.dequeue()
	qeueue.dequeue()
}

type Queue struct {
	fifo []int
	lifo []int
}

func (receiver *Queue) enqueue(element int) {
	// 末尾追加
	receiver.fifo = append(receiver.fifo, element)

	// 先頭追加
	receiver.lifo = append([]int{element}, receiver.lifo...)
}

func (receiver *Queue) dequeue() {
	// fifo
	// プリミティブなので、不要な配列の要素にゼロ値を入れる必要はない
	if receiver.fifo != nil {
		receiver.fifo = receiver.fifo[1:len(receiver.fifo)]
	}

	// lifo
	if receiver.lifo != nil {
		receiver.lifo = receiver.lifo[:len(receiver.lifo)-1]
	}
}

func (receiver *Queue) print() {
	// fifo
	if receiver.fifo != nil {
		fmt.Printf("fifo: %d\n", receiver.fifo[0])
	}

	if receiver.lifo != nil {
		fmt.Printf("lifo: %d\n", receiver.lifo[len(receiver.lifo)-1])
	}
}
