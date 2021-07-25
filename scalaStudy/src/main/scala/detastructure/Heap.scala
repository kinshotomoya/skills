package detastructure

import scala.collection.mutable

object Heap {

  // practice1

  object Solution {
    var count: Int = 0
    val heap: mutable.ArrayBuffer[String] = scala.collection.mutable.ArrayBuffer.empty[String]

    def main(args: Array[String]): Unit = {
      if(count != 0) {
        count += 1
        val queryNum = args(0)
        queryNum match {
          case "1" => insertValue(args(1))
          case "2" => deleteValue(args(1))
          case "3" => printMinNum
          case _ =>
        }
      }
    }

    def insertValue(ele: String): Unit = {
      // insertは、とりあえすarray末尾に要素を追加して、親要素との大小比較で上位レイヤーに移動させていく
      heap.addOne(ele)

      @scala.annotation.tailrec
      def loop(currentEle: String, currentEleIndex: Int): Unit = {
        val parentEleIndex = Math.ceil((currentEleIndex - 1) / 2).toInt
        if(parentEleIndex < 0) {
          // parentがいないので、何もしない
        } else {
          val parentEle = heap(parentEleIndex)
          if(parentEle < currentEle) {
            // parentの方が小さければ移動も何もしない
          } else {
            // currentの方が小さければ、parentをswitchする
            heap(parentEleIndex) = currentEle
            heap(currentEleIndex) = parentEle
            loop(currentEle, parentEleIndex)
          }
        }
      }

      loop(heap.last, heap.length - 1)

    }

    def deleteValue(ele: String): Unit = {
      val deletedEleIndex = heap.indexOf(ele)
      heap(deletedEleIndex) = heap.last

      var currentEleIndex = deletedEleIndex
      val currentEle = heap(deletedEleIndex)
      val parentEleIndex = Math.ceil((deletedEleIndex - 1) / 2).toInt
      if(parentEleIndex < 0) {
        // parentがいないので何もしない
      } else {
        val parentEle = heap(parentEleIndex)
        if(parentEle < currentEle) {
          // 左右子ノードとの大小比較をする
          val leftChildEleIndex = 2*currentEleIndex + 1
          val rightChildEleIndex = 2*currentEleIndex + 2
          if(leftChildEleIndex > heap.length-1 && rightChildEleIndex > heap.length-1) {
            // 左右子ノードはいないので何もしない
          } else if(leftChildEleIndex < heap.length-1 && rightChildEleIndex > heap.length-1) {
            // 左子ノードのみ存在する場合
            val leftChildEle = heap(leftChildEleIndex)
            if(currentEle < leftChildEle) {
              // 何もしない
            } else {
              heap(leftChildEleIndex) = currentEle
              heap(currentEleIndex) = leftChildEle
              currentEleIndex = leftChildEleIndex
              // loop
            }
          } else if(leftChildEleIndex > heap.length-1 && rightChildEleIndex < heap.length-1) {
            // 右子ノードのみ存在する場合
            val rightEle = heap(rightChildEleIndex)
            if(currentEle < rightEle) {
              // 何もしない
            } else {
              heap(rightChildEleIndex) = currentEle
              heap(currentEleIndex) = rightEle
              currentEleIndex = rightChildEleIndex
              // loop
            }
          } else {
            // どっちも存在する場合
            val leftEle = heap(leftChildEleIndex)
            val rightEle = heap(rightChildEleIndex)
            if(currentEle < leftEle && currentEle < rightEle) {
              if(leftEle < rightEle) {
                // 左子ノードと入れ替える
                heap(leftChildEleIndex) = currentEle
                heap(currentEleIndex) = leftEle
                currentEleIndex = leftChildEleIndex
                // loop
              } else {
                // 右子ノードと入れ替える
                heap(rightChildEleIndex) = currentEle
                heap(currentEleIndex) = rightEle
                currentEleIndex = rightChildEleIndex
                // loop
              }
            } else if (currentEle < leftEle) {
              heap(leftChildEleIndex) = currentEle
              heap(currentEleIndex) = leftEle
              currentEleIndex = leftChildEleIndex
              // loop
            } else if(currentEle < rightEle) {
              heap(rightChildEleIndex) = currentEle
              heap(currentEleIndex) = rightEle
              currentEleIndex = rightChildEleIndex
              // loop
            } else {
              // currentEleの方が小さいので何もしない
            }
          }
        } else {
          heap(parentEleIndex) = currentEle
          heap(deletedEleIndex) = parentEle
          currentEleIndex = parentEleIndex
          // loop
        }
      }

      // 引数で渡されたeleをfor loopで探してきて、そいつを削除
      // その場所への埋め合わせとして、最下層の右ノードをその場所に持ってくる
      // ここまでで、heapの構造は保てているが、heap order ruleが崩れている可能性があるので要素を入れ替える
      // まず、親ノードとの大小比較を行う
      // 親ノードよりも小さければ、親ノードと入れ替える（これを繰り返す）
      // 親ノードよりも大きければ、今度は右・左の子ノードと大小比較をする
      // 右・左子ノードよりも小さければ、そのまま、大きければ、より小さい方の子ノードと入れ替える（繰り返す）
    }

    def printMinNum: Unit = {
      println(heap.head)
    }

  }


}
