package internal

import (
	"context"
	"fmt"
	"github.com/redis/go-redis/v9"
	"time"
)

type RedisClient struct {
	redisClient *redis.Client
}

func NewRedisClient() *RedisClient {
	option := &redis.Options{
		Addr:     "localhost:6379",
		Password: "",
		DB:       0,
	}
	client := redis.NewClient(option)
	return &RedisClient{
		redisClient: client,
	}
}

type RateLimiter struct {
	LimitTimeMill int64        // 制限時間ms
	LimitCount    int64        // 制限時間内のリクエスト上限回数
	RedisClient   *RedisClient // rateLimitを実現するためのredisクライアント
}

func NewRateLimiter() *RateLimiter {
	return &RateLimiter{
		LimitTimeMill: 5 * time.Second.Milliseconds(), // 5秒のms
		LimitCount:    3,                              //3回
		RedisClient:   NewRedisClient(),
	}
}

func (r *RateLimiter) CheckLimit(ctx context.Context, key string, nowTimeStampMill int64) bool {
	scoreMember := redis.Z{
		Score:  float64(nowTimeStampMill),
		Member: float64(nowTimeStampMill),
	}
	// とりあえずredisにデータを追加
	_, err := r.RedisClient.redisClient.ZAdd(ctx, key, scoreMember).Result()
	if err != nil {
		return false
	}
	// 対象keyの1分前までのデータを削除する
	_, err = r.RedisClient.redisClient.ZRemRangeByScore(ctx, key, "0", fmt.Sprintf("%d", nowTimeStampMill-r.LimitTimeMill)).Result()
	if err != nil {
		return false
	}
	// 対象keyのredisに残っているデータ数を取得する
	count, err := r.RedisClient.redisClient.ZCount(ctx, key, "-inf", "+inf").Result()
	println(count)
	if err != nil {
		println(err)
		return false
	}

	// 上限以内ならOK（true）を返す
	return count <= r.LimitCount

}
