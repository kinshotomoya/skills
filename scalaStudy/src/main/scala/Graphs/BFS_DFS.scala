package Graphs

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

object BFS_DFS {


  def BFS(graph: Graph): Unit = {
    // mutable.Queueは、内部的にArrayDequeをラップしている
    // なので、enqueueメソッドでも結局内部的にはArrayDequeueのaddOneメソッドが呼ばれているだけ
    val nodeQueue = mutable.Queue.empty[Int]
    val seenNodeSet = mutable.Set.empty[Int]
    graph.list.zipWithIndex.foreach((indexWithArray: (ArrayBuffer[Int], Int)) => {
      val parentNode = indexWithArray._2
      val childNodes = indexWithArray._1

      nodeQueue.enqueue(parentNode)
      nodeQueue.enqueueAll(childNodes)

      for(_ <- 1 to nodeQueue.size) {
        val targetEle = nodeQueue.dequeue()
        if(!seenNodeSet.contains(targetEle)) {
          println(targetEle)
          seenNodeSet.add(targetEle)
        }

      }

    })

  }


  def DFS(graph: Graph): Unit = {
    val nodeStack = mutable.Stack.empty[Int]
    val seenNodeSet = mutable.Set.empty[Int]

    def loop(childNodes: ArrayBuffer[Int]): Unit = {
      if(nodeStack.nonEmpty) {
        val targetNode = nodeStack.pop()
        if(!seenNodeSet.contains(targetNode)) {
          println(targetNode)
          seenNodeSet.add(targetNode)
        }
        // pushAllの内部でtraverseがreverseされている
        // ex) (2, 3, 4)のArrayをpushAllでstackに格納すると、一番上が4になる（つまり4が最初にpopされる）
        nodeStack.pushAll(childNodes.filter(node => !seenNodeSet.contains(node)))
        val nextTarget = if(targetNode == 0) {
          nodeStack.push(1)
          1
        } else nodeStack.headOption.getOrElse(-1)
        if(nextTarget != -1) loop(graph.list(nextTarget))
      }
    }
    nodeStack.push(0)
    loop(graph.list(0))

  }



  // edgeは頂点を紐づける線のこと
  // sourceは自分自身の頂点
  // destは子要素として紐づく頂点
  case class Edge(source: Int, dest: Int)

  // ↑のグラフをbfsで標準出力する

  case class Graph(list: Array[ArrayBuffer[Int]])

  object Graph {
    def apply(edges: Array[Edge], numberOfNodes: Int): Graph = {
      // 固定サイズのArrayを作成
      val outLineList: Array[ArrayBuffer[Int]] = new Array[ArrayBuffer[Int]](numberOfNodes + 1)

      // [[], [], []...]みたいなgraph構造の側だけ作成
      for(i <- 0 to numberOfNodes) {
        // ArrayBufferは内部的にnew Arrayをラップしており
        // ArrayBufferを使うことで、サイズとか諸々考えずにmutableなArrayを利用することができる
        val innerList: ArrayBuffer[Int] = ArrayBuffer.empty[Int]
        outLineList.update(i, innerList)
      }

      // 実際に[[], [2, 3, 4], [5, 6], ...]みたいなデータ構造でgraphを表現する
      edges.foreach(edge => {
        val source = edge.source
        val dest = edge.dest

        // 例えば、ここのaddOneでも、生のArrayを利用していると
        // 自分のサイズをまず取得して、その次のindexに要素を追加する
        // さらに自分のArrayサイズがリサイズしなくても良いのかなどの判断も必要になる
        outLineList.apply(source).addOne(dest)
        outLineList.apply(dest).addOne(source)

      })

      Graph(outLineList)

    }
  }


  def main(args: Array[String]): Unit = {

    val edges = Array(
      Edge(1, 2),
      Edge(1, 3),
      Edge(1, 4),
      Edge(2, 5),
      Edge(2, 6),
      Edge(5, 9),
      Edge(5, 10),
      Edge(4, 7),
      Edge(4, 8),
      Edge(7, 11),
      Edge(7, 12),
    )

    val numberOfNodes = 12


    val graph = Graph(edges, numberOfNodes)
    BFS(graph)
  }

}
