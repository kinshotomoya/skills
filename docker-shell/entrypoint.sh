#!/bin/bash


handle_signal() {
    echo 'SIGINT/SIGTERMを検出しました。'
    # メインプロセスの子プロセスID一覧を取得
    local children=`ps --ppid $$ --no-heading | awk '{ print $1 }'`
    for child in $children
    do
        echo $child
        # 明示的に子プロセスを終了させる
        kill –s TERM $child
    done
    exit 0
}

trap handle_signal INT TERM

while true; do
  sleep 1
done