package internal

import (
	"fmt"
	"net/http"
)

type Result struct {
	Error    error
	Response *http.Response
}

func ErrorHandling() {

	checkStatus := func(done <-chan any, urls ...string) <-chan Result {
		results := make(chan Result)
		go func() {
			defer close(results)
			for _, url := range urls {
				resp, err := http.Get(url)
				result := Result{Error: err, Response: resp}
				select {
				case <-done:
					return
				case results <- result: // ここでresultをチャネルに書き込んでいる
				}
			}
		}()
		return results
	}

	doneChan := make(chan any)
	defer close(doneChan)
	urls := []string{"https://google.com", "hogehoge"}
	for result := range checkStatus(doneChan, urls...) {
		if result.Error != nil {
			fmt.Printf("error %v", result.Error)
			continue
		}
		fmt.Printf("response: %v", result.Response.Status)
	}

}
