package main

import (
	"bufio"
	"fmt"
	"os"
	"strings"
	"unicode"
)

func Tests() {
	//Enter your code here. Read input from STDIN. Print output to STDOUT
	scanner := bufio.NewScanner(os.Stdin)

	kinds := make(map[string]struct{})
	for scanner.Scan() {
		text := scanner.Text()
		arrayText := strings.Split(text, " ")
		for i := range arrayText {
			tango := arrayText[i]
			capitalLetter := arrayText[i][0]
			runeCapitalLetter := rune(capitalLetter)
			if unicode.IsUpper(runeCapitalLetter) || unicode.IsDigit(runeCapitalLetter) {
				_, exist := kinds[tango]
				if !exist {
					kinds[tango] = struct{}{}
				}
			}
		}
	}
	fmt.Println(len(kinds))
}

// 実際に提出したコード
func test() {
	scanner := bufio.NewScanner(os.Stdin)

	kinds := make(map[string]struct{})
	for scanner.Scan() {
		text := scanner.Text()
		arrayText := strings.Split(text, " ")
		for i := range arrayText {
			tango := arrayText[i]
			capitalLetter := arrayText[i][0]
			runeCapitalLetter := rune(capitalLetter)
			if unicode.IsUpper(runeCapitalLetter) || unicode.IsDigit(runeCapitalLetter) {
				_, exist := kinds[tango]
				if !exist {
					kinds[tango] = struct{}{}
				}
			}
		}
	}
	fmt.Println(len(kinds))
}

// 改善版のコード
func improveTest() {
	scanner := bufio.NewScanner(os.Stdin)

	// 改善点：
	// mapのkeyをstringからrune（int32、unicodeコードポインタ）に変更することでメモリ利用率を抑えれる
	// 理由：
	//   stringの構造は以下のようになっている
	// type stringStruct struct {
	//    str unsafe.Pointer
	//    len int
	//}
	// unsafe.Pointerは、メモリへのポインタで64bit環境だとワードサイズになるので64bit（8byte）
	// lenは文字列そもそもの長さなのでintで、64bit環境だと64bit（byte）になる
	// なので、runeに変更した方がメモリ利用率は抑えることができる

	// 意識した点：
	// hashsetを作る際に、空の構造体を指定
	// 空の構造体はメモリを利用しないのでメモリ効率が良い
	kinds := make(map[rune]struct{})
	for scanner.Scan() {
		text := scanner.Text()
		arrayText := strings.Split(text, " ")
		for i := range arrayText {
			capitalLetter := arrayText[i][0]
			runeCapitalLetter := rune(capitalLetter)
			if unicode.IsUpper(runeCapitalLetter) || unicode.IsDigit(runeCapitalLetter) {
				_, exist := kinds[runeCapitalLetter]
				if !exist {
					kinds[runeCapitalLetter] = struct{}{}
				}
			}
		}
	}
	fmt.Println(len(kinds))
}
