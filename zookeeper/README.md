
```shell
# zookeeperの１つのノードに入る
$ docker exec -it zookeeper-zoo1-1 bin/zkCli.sh
[zk: localhost:2181(CONNECTED) 0]


[zk: localhost:2181(CONNECTED) 0] create /znode
Created /znode
[zk: localhost:2181(CONNECTED) 0] create /znode/node
Created /znode
# ちゃんとファイルシステムみたいにディレクトリ？が作成されているいることがわかる
[zk: localhost:2181(CONNECTED) 0] ls /znode
[node]


# 他のノードに入る
$ docker exec -it zookeeper-zoo2-1 bin/zkCli.sh

# 確認すると、同期されていることがわかる！
[zk: localhost:2181(CONNECTED) 0] ls /znode
[node]
```
