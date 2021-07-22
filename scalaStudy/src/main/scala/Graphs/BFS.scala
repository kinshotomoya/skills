package Graphs

import scala.collection.mutable.ArrayBuffer

object BFS {


  def BFS(graph: Graph): Unit = {
    graph.list.map(_.toArray)
    ???
  }

  // graphのbfsを実装してみる


  // まず、graphの状態をコード化する

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
