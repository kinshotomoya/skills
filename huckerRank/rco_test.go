package main

//
//func RunPorker() {
//	a := 1
//	b := 2
//	c := 4
//
//	// 1, 3, 5
//	// 2, 3
//	cards := map[int]struct{}{1: {}, 2: {}, 3: {}, 4: {}, 5: {}}
//	delete(cards, b)
//	delete(cards, c)
//	// Aを考える
//	for {
//		res := make([]bool, 3)
//		for key := range cards {
//			if isMin(b, c, key) {
//				res[0] = true
//			} else if isMiddle(b, c, key) {
//				res[1] = true
//			} else if isMax(b, c, key) {
//				res[2] = true
//			}
//		}
//
//	}
//
//}
//
//func isMin(b int, c int, self int) bool {
//	return self < b && self < c
//}
//
//func isMiddle(b int, c int, self int) bool {
//	return (b < self && self < c) || (c < self && self < b)
//}
//
//func isMax(b int, c int, self int) bool {
//	return b < self && c < self
//}
