package main

import "strings"

func IsBalanced(expression string) string {
	expressionArray := strings.Split(expression, "")
	bracketMap := map[string]string{"(": ")", "{": "}", "[": "]"}
	stack := make([]string, len(expressionArray)/2)
	middleIndex := len(expressionArray)/2 - 1
	for i := range expressionArray {
		if i <= middleIndex {
			stack[i] = expressionArray[i]
		} else {
			bracket, _ := bracketMap[stack[len(stack)-1]]
			if bracket == expressionArray[i] {
				// sliceで使われなくなっても基底配列全体が参照されなくなるまで各値はGCされないので
				// もう不要な値に関しては、基底配列にゼロ値を入れてあげて参照を切ってあげることでGC対象にする
				// 参考：https://go.dev/blog/slices-intro
				stack[len(stack)-1] = ""
				stack = stack[:len(stack)-1]
				continue
			} else {
				return "NO"
			}
		}
	}
	return "YES"

}
