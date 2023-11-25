package main

type Node struct {
	next *Node
}

func DetectCycle(head *Node, paths map[*Node]struct{}) bool {
	if head == nil {
		return false
	}
	_, exist := paths[head]
	if exist {
		return true
	} else {
		paths[head] = struct{}{}
	}

	return DetectCycle(head.next, paths)
}
