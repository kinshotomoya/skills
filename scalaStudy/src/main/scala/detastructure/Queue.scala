package detastructure

object Queue {
  class MyCircularQueue(_k: Int) {

    class Node(_data: Int, _next: Node, _pointerToHead: Node) {
      var data: Int = _data
      // last Nodeは、headへのpointerを保持する
      var next: Node = _next
      var indexNum: Int = 0
      var pointerToHead: Node = _pointerToHead
    }

    val queue: Array[Node] = Array.fill(_k)(null)
    var head: Node = null
    var tail: Node = null

    def enQueue(value: Int): Boolean = {
      if(isFull()) {
        false
      } else {
        val newNode = new Node(_data = value, _pointerToHead = head, _next = null)
        if(head == null) {
          // queueが空の場合
          head = newNode
          tail = newNode
          queue(0) = newNode
          true
        } else if(tail.indexNum == queue.length - 1) {
          // tailがarrayの末尾の場合
          var loop = true
          var index = 0
          while(loop) {
            if(queue(index) == null) {
              newNode.indexNum = index
              tail.next = newNode
              tail = newNode
              queue(index) = newNode
              loop = false
            } else {
              index += 1
            }
          }
          true
        } else {
          // tailがarrayの途中の場合
          // tailのindexNumの次になるので
          val newNodeIndexNum = tail.indexNum + 1
          // 新しく追加するnodeのindexNumをtailの次のindexに設定する
          newNode.indexNum = newNodeIndexNum
          tail.next = newNode
          tail = newNode
          queue(newNodeIndexNum) = newNode
          true
        }
      }
    }

    def deQueue(): Boolean = {
      if(isEmpty()) {
        false
      } else {
        queue.update(head.indexNum, null)
        if(head == tail) {
          head = null
          tail = null
        } else {
          head = head.next
        }
        true
      }
    }

    def Front(): Int = {
      if(isEmpty()) {
        -1
      } else {
        head.data
      }
    }

    def Rear(): Int = {
      if(isEmpty()) {
        -1
      } else {
        tail.data
      }
    }

    def isEmpty(): Boolean = {
      head == null && tail == null
    }

    def isFull(): Boolean = {
      !queue.contains(null)
    }

  }



  class MyCircularQueue2(_k: Int) {
    val queue = Array.fill(_k)(0)
    var head = 0
    var tail = 0
    var size = 0


    def enQueue(value: Int): Boolean = {
      if(isFull()) {
        false
      } else {
        queue(tail) = value
        tail = (tail + 1) % _k
        size += 1
        true
      }
    }

    def deQueue(): Boolean = {
      if(isEmpty()) {
        false
      } else {
        queue(head) = 0
        head = (head + 1) % _k
        size -= 1
        true
      }
    }

    def Front(): Int = {
      if(isEmpty()) {
        -1
      } else {
        queue(head)
      }
    }

    def Rear(): Int = {
      if(isEmpty()) {
        -1
      } else {
        queue((tail - 1 + _k) % _k)
      }
    }

    def isEmpty(): Boolean = {
      size == 0
    }

    def isFull(): Boolean = {
      size == _k
    }


  }

}
