package main

import (
	"bufio"
	"fmt"
	"io"
	"os"
	"strconv"
	"strings"
)

// [1 2 3 4 5] 3
// -> [2 3 4 5 1]
// -> [3 4 5 1 2]

// 問題：https://www.hackerrank.com/challenges/ctci-array-left-rotation/problem?h_r=internal-search
func rotLeft(a []int32, d int32) []int32 {
	// Write your code here
	newArray := make([]int32, len(a))
	if len(a)%int(d) == 0 {
		// len(array)の倍数の場合は同じ位置になる
		return a
	} else if len(a)/int(d) == 0 {
		// dがlen(a)よりも小さい場合
		for i := range a {
			if i >= int(d) {
				newArray[i-int(d)] = a[i]
			} else {
				newArray[i+len(a)-int(d)] = a[i]
			}
		}
	} else {
		// dがlen(array)よりも大きい場合
		amari := len(a) % int(d)
		for i := range a {
			if i >= amari {
				newArray[i-amari] = a[i]
			} else {
				newArray[i+len(a)-amari] = a[i]
			}
		}
	}

	return newArray

	//d = 6の場合（あまり1）
	//4 -> 3
	//3 -> 2
	//2 -> 1
	//1 -> 0
	//0 -> 4

	//d = 7の場合（あまり2）
	//4 -> 2
	//3 -> 1
	//2 -> 0
	//1 -> 4
	//0 -> 3

}

func LeftRotation() {
	reader := bufio.NewReaderSize(os.Stdin, 16*1024*1024)

	stdout, err := os.Create(os.Getenv("OUTPUT_PATH"))
	checkError(err)

	defer stdout.Close()

	writer := bufio.NewWriterSize(stdout, 16*1024*1024)

	firstMultipleInput := strings.Split(strings.TrimSpace(readLine(reader)), " ")

	nTemp, err := strconv.ParseInt(firstMultipleInput[0], 10, 64)
	checkError(err)
	n := int32(nTemp)

	dTemp, err := strconv.ParseInt(firstMultipleInput[1], 10, 64)
	checkError(err)
	d := int32(dTemp)

	aTemp := strings.Split(strings.TrimSpace(readLine(reader)), " ")

	var a []int32

	for i := 0; i < int(n); i++ {
		aItemTemp, err := strconv.ParseInt(aTemp[i], 10, 64)
		checkError(err)
		aItem := int32(aItemTemp)
		a = append(a, aItem)
	}

	result := rotLeft(a, d)

	for i, resultItem := range result {
		fmt.Fprintf(writer, "%d", resultItem)

		if i != len(result)-1 {
			fmt.Fprintf(writer, " ")
		}
	}

	fmt.Fprintf(writer, "\n")

	writer.Flush()
}

func readLine(reader *bufio.Reader) string {
	str, _, err := reader.ReadLine()
	if err == io.EOF {
		return ""
	}

	return strings.TrimRight(string(str), "\r\n")
}

func checkError(err error) {
	if err != nil {
		panic(err)
	}
}
