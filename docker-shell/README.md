
entrypoint.shのラッパースクリプトが親プロセス
entrypoint.shスクリプト内で実行しているsleepコマンドが子プロセスで起動している

```shell
$ docker exec 0ee68f97b963 ps -ef
UID        PID  PPID  C STIME TTY          TIME CMD
root         1     0  0 16:12 ?        00:00:00 /bin/bash ./entrypoint.sh
root        26     1  0 16:12 ?        00:00:00 sleep 1
root        27     0  0 16:12 ?        00:00:00 ps -ef

# コンテナを止める
# PID1に対してSIGTERMが送られる
$ docker stop <container id>


# entrypoint.shではシグナルハンドリング実装しているので、↓のようなテキストが出力される
SIGINT/SIGTERMを検出しました。
```

