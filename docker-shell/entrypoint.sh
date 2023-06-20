#!/bin/bash


handle_signal() {
    echo 'SIGINT/SIGTERMを検出しました。'
    exit 0
}

trap handle_signal INT TERM

while true; do
  sleep 1
done