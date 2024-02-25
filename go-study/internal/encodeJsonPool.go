package internal

import (
	"bytes"
	"encoding/json"
	"sync"
	"time"
)

type JsonData struct {
	UserName    string
	UserID      int
	Email       string
	DateOfBirth time.Time
	Address     string
	PhoneNumber string
	UserStatus  bool
	LastLogin   time.Time
	Products    []ProductInfo
}

type ProductInfo struct {
	ProductName  string
	ProductID    int
	Manufacturer string
	Price        float64
	InStock      bool
	ReleaseDate  time.Time
	Category     string
	Rating       float64
}

func EncodeJson(jsonDate *JsonData) {
	var buf bytes.Buffer
	err := json.NewEncoder(&buf).Encode(jsonDate)
	if err != nil {
		return
	}
	//fmt.Printf("jsonDate: %s", buf.String())
}

var byteBufferPool = &sync.Pool{
	New: func() any {
		return &bytes.Buffer{}
	},
}

func EncodeJsonPool(jsonData *JsonData) {
	buf := byteBufferPool.Get().(*bytes.Buffer)
	err := json.NewEncoder(buf).Encode(jsonData)
	defer buf.Reset() // 中身をリセットする
	if err != nil {
		return
	}
	//fmt.Printf("jsonDate: %s", buf.String())
	byteBufferPool.Put(buf)
}
