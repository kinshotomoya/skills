package main

import (
	"context"
	"fmt"
	"time"
)

func main() {

	//execRateLimit()
	//execReceiveChannel()
	//execGoroutineSelect()
	execGoroutinePattern()
}

func execRateLimit() {
	ctx := context.Background()
	rateLimiter := NewRateLimiter()

	key := "userid: 1"

	// 一回目
	isOk := rateLimiter.CheckLimit(ctx, key, time.Now().UnixMilli())
	fmt.Printf("checkLimit1: %v", isOk)
	// 2回目
	isOk = rateLimiter.CheckLimit(ctx, key, time.Now().UnixMilli())
	fmt.Printf("checkLimit2: %v", isOk)
	// 3回目
	isOk = rateLimiter.CheckLimit(ctx, key, time.Now().UnixMilli())
	fmt.Printf("checkLimit3: %v", isOk)
	// 4回目
	// これがfalseになるはず
	isOk = rateLimiter.CheckLimit(ctx, key, time.Now().UnixMilli())
	fmt.Printf("checkLimit4: %v", isOk)

	// TODO: 複数goroutineを起動してratelimitをためしてみる
}

func execReceiveChannel() {
	receiveChannel()
}

func execGoroutineSelect() {
	selectGoroutine()
}

func execGoroutinePattern() {
	//pattern3()
	pattern4()
}
