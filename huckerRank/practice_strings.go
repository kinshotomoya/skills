package main

import (
	"encoding/csv"
	"fmt"
	"io"
	"log"
	"os"
	"strconv"
	"strings"
)

func CountCapitalLetters(text string) {
	// 頭文字とその位置を標準出力させる
	// i am a software engineer
	textArray := strings.Split(text, " ")
	var preCount int
	for i := range textArray {
		capitalLetter := strings.Split(textArray[i], "")[0]
		count := preCount + 1
		fmt.Printf("capitalLetter: %s, count: %d \n", capitalLetter, count)
		preCount += len(textArray[i])
	}
}

func CountEvenNumber(text string) {
	// 偶数もしくは、１つ前の数と同じ数値を標準出力させる
	// 1,2,3,3,4,5,6,6,6,6,6,6,6
	numbers := strings.Split(text, ",")
	var preNumber int
	for i := range numbers {
		intNum, _ := strconv.Atoi(numbers[i])
		if intNum%2 == 0 || intNum == preNumber {
			fmt.Println(numbers[i])
		}
		preNumber = intNum
	}

}

func CalculateEachSubjectInCsv() {
	file, err := os.Open("sample.csv")
	if err != nil {
		log.Fatalf("err read csv file %s", err)
	}
	defer file.Close()

	reader := csv.NewReader(file)
	for {
		record, err := reader.Read()
		if err == io.EOF {
			break
		}
		count := len(record)
		var scores int
		for i := range record {
			intScore, _ := strconv.Atoi(record[i])
			scores += intScore
		}
		fmt.Println(scores / count)
	}

}
