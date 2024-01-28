package main

import (
	"bytes"
	"testing"
	"time"
)

var products = []ProductInfo{
	{
		ProductName:  "Product A",
		ProductID:    101,
		Manufacturer: "Company A",
		Price:        19.99,
		InStock:      true,
		ReleaseDate:  time.Date(2023, 1, 10, 0, 0, 0, 0, time.UTC),
		Category:     "Electronics",
		Rating:       4.5,
	},
	{
		ProductName:  "Product B",
		ProductID:    102,
		Manufacturer: "Company B",
		Price:        29.99,
		InStock:      false,
		ReleaseDate:  time.Date(2023, 3, 15, 0, 0, 0, 0, time.UTC),
		Category:     "Books",
		Rating:       4.7,
	},
}

var jsonData = JsonData{
	UserName:    "John Doe",
	UserID:      12345,
	Email:       "johndoe@example.com",
	DateOfBirth: time.Date(1980, 12, 15, 0, 0, 0, 0, time.UTC),
	Address:     "123 Main St, Anytown, AN 12345",
	PhoneNumber: "123-456-7890",
	UserStatus:  true,
	LastLogin:   time.Now(),
	Products:    products,
}

func BenchmarkEncodeJson(b *testing.B) {
	b.ResetTimer()
	for i := 0; i < b.N; i++ {
		EncodeJson(&jsonData)
	}
}

func BenchmarkEncodeWithPoolJson(b *testing.B) {
	b.ResetTimer()
	byteBufferPool.Put(&bytes.Buffer{})
	byteBufferPool.Put(&bytes.Buffer{})
	byteBufferPool.Put(&bytes.Buffer{})
	byteBufferPool.Put(&bytes.Buffer{})
	byteBufferPool.Put(&bytes.Buffer{})
	byteBufferPool.Put(&bytes.Buffer{})
	byteBufferPool.Put(&bytes.Buffer{})
	byteBufferPool.Put(&bytes.Buffer{})
	for i := 0; i < b.N; i++ {
		EncodeJsonPool(&jsonData)
	}
}
