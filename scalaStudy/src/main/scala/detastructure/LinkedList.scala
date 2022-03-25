//package detastructure
//
//import scala.collection.mutable.ListBuffer
//
//object LinkedList {
//  class MyLinkedList() {
//
//    /** Initialize your data structure here. */
//
//    class Node(val value: Int, var next: Option[Node])
//    val linkedList = new Node(-1, None)
//
//
//    /** Get the value of the index-th node in the linked list. If the index is invalid, return -1. */
//    def get(index: Int): Int = {
//      def loop(target: Option[Node], index: Int): Int ={
//        target match {
//          case None => -1
//          case Some(node) =>
//            if(index == 0) {
//              node.value
//            } else {
//              loop(target.flatMap(_.next), index - 1)
//            }
//        }
//      }
//      loop(linkedList.next, index)
//    }
//
//    /** Add a node of value val before the first element of the linked list. After the insertion, the new node will be the first node of the linked list. */
//    def addAtHead(`val`: Int): Unit = {
//      val newNode = new Node(`val`, linkedList.next)
//      linkedList.next = Some(newNode)
//    }
//
//    /** Append a node of value val to the last element of the linked list. */
//    def addAtTail(`val`: Int): Unit = {
//      def loop(previous: Option[Node], target: Option[Node]): Unit = {
//        target match {
//          case None =>
//            val newNode = new Node(`val`, None)
//            previous.foreach(_.next = Some(newNode))
//          case Some(node) => loop(target, node.next)
//        }
//      }
//      loop(Some(linkedList), linkedList.next)
//
//    }
//
//    /** Add a node of value val before the index-th node in the linked list. If index equals to the length of linked list, the node will be appended to the end of linked list. If index is greater than the length, the node will not be inserted. */
//    def addAtIndex(index: Int, `val`: Int): Unit = {
//      def loop(previous: Option[Node], target: Option[Node], index: Int): Unit = {
//        target match {
//          case None =>
//            if(index == 0) {
//              previous.foreach(_.next = Some(new Node(`val`, None)))
//            } else {
//              // not insert
//            }
//          case Some(_) =>
//            if(index == 0) {
//              val newNode = new Node(`val`, target)
//              previous.foreach(_.next = Some(newNode))
//            } else {
//              loop(target, target.flatMap(_.next), index - 1)
//            }
//        }
//      }
//      loop(Some(linkedList), linkedList.next, index)
//    }
//
//    /** Delete the index-th node in the linked list, if the index is valid. */
//    def deleteAtIndex(index: Int): Unit = {
//      def loop(previous: Option[Node], target: Option[Node], index: Int): Unit = {
//        target match {
//          case None =>
//          case Some(node) =>
//            if(index == 0) {
//              previous.foreach(_.next = node.next)
//            } else {
//              loop(target, target.flatMap(_.next), index - 1)
//            }
//        }
//      }
//
//      loop(Some(linkedList), linkedList.next, index)
//
//    }
//
//  }
//
//  val aa = new ListBuffer[Int]()
//  aa.last
//  aa.length
//  aa.head
//  val bb = List(1)
//  bb.last
//
//  /**
//   * Your MyLinkedList object will be instantiated and called as such:
//   * var obj = new MyLinkedList()
//   * var param_1 = obj.get(index)
//   * obj.addAtHead(`val`)
//   * obj.addAtTail(`val`)
//   * obj.addAtIndex(index,`val`)
//   * obj.deleteAtIndex(index)
//   */
//}
//
//
//
//
//object CircleLinkedList {
//
//
//  class ListNode(var _x: Int = 0) {
//    var next: ListNode = null
//    var x: Int = _x
//  }
//
//  val hashSet = new scala.collection.mutable.HashSet[ListNode]()
//
//
//  // solution1
//  def hasCycle(head: ListNode): Boolean = {
//
//    def loop(node: ListNode): Boolean = {
//      if(node == null) {
//        false
//      } else {
//        if(hashSet.contains(node)) {
//          true
//        } else {
//          hashSet.add(node)
//          loop(node.next)
//        }
//      }
//    }
//    loop(head)
//  }
//
//
//  // solution2
//  def hasCycle2(head: ListNode): Boolean = {
//
//    def loop(slow: ListNode, fast: ListNode): Boolean = {
//      if(slow == fast) {
//        true
//      } else {
//        if(fast == null || fast.next == null) {
//          false
//        } else {
//          loop(slow.next, fast.next.next)
//        }
//      }
//    }
//
//    if(head == null) {
//      false
//    } else {
//      loop(head, head.next)
//    }
//
//  }
//
//
//}
//
//
//object CircleLinkedList2 {
//  class ListNode(var _x: Int = 0) {
//    var next: ListNode = null
//    var x: Int = _x
//  }
//
//  val hashSet = new scala.collection.mutable.HashSet[ListNode]()
//
//  def detectCycle(head: ListNode): ListNode = {
//
//    def loop(node: ListNode): ListNode = {
//      if(node == null) {
//        null
//      } else {
//        val next = node.next
//        hashSet.find(_ == next) match {
//          case None =>
//            hashSet.add(node)
//            loop(next)
//          case Some(n) => n
//        }
//      }
//    }
//
//    loop(head)
//
//  }
//
//
//}
//
//object interSectionLinkedList {
//  class ListNode(var _x: Int = 0) {
//    var next: ListNode = null
//    var x: Int = _x
//  }
//
//  val hashSet = new scala.collection.mutable.HashSet[ListNode]()
//
//  // BのエレメントをhashSetにつめていき、Aを一個づつ見てhashSetに存在するかしない判断する
//  def getIntersectionNode(headA: ListNode, headB: ListNode): ListNode = {
//
//
//    def loopPut(nodeB: ListNode): Unit = {
//      if(nodeB != null) {
//        hashSet.add(nodeB)
//        loopPut(nodeB.next)
//      }
//    }
//
//
//    def loop(nodeA: ListNode): ListNode = {
//
//      if(nodeA != null) {
//        if(hashSet.contains(nodeA)) {
//          nodeA
//        } else {
//          loop(nodeA.next)
//        }
//      } else {
//        null
//      }
//    }
//
//    loopPut(headB)
//    loop(headA)
//
//  }
//}
//
//
//object interSectionLinkedList2 {
//  class ListNode(var _x: Int = 0) {
//    var next: ListNode = null
//    var x: Int = _x
//  }
//
//
//  def getIntersectionNode(headA: ListNode, headB: ListNode): ListNode = {
//
//    // intersectionが存在しない場合にも、pointerA, pointerB共に同時にnullを指すようになるので、
//    // 特別な処理はいらない
//    def loop(pointerA: ListNode, pointerB: ListNode): ListNode = {
//      var x = pointerA
//      var y = pointerB
//      if(x == y) {
//        pointerA
//      } else {
//        x = if(pointerA == null) headB else x.next
//        y = if(pointerB == null) headA else y.next
//        loop(x, y)
//      }
//    }
//    loop(headA, headB)
//  }
//}
//
//
//object RemoveNthNode {
//  class ListNode(var _x: Int = 0) {
//    var next: ListNode = null
//    var x: Int = _x
//  }
//
//  def removeNthFromEnd(head: ListNode, n: Int): ListNode = {
//
//    def countLength(node: ListNode, length: Int): Int = {
//      if(node == null) {
//        length
//      } else {
//        countLength(node.next, length+1)
//      }
//    }
//
//    val listNodeLength = countLength(head, 0)
//
//    def loop(previous: ListNode, node: ListNode, nth: Int): ListNode = {
//      if(nth == 1) {
//        if(previous == 0) {
//          node.next
//        } else {
//          previous.next = node.next
//          head
//        }
//      } else {
//        loop(node, node.next, nth-1)
//      }
//    }
//
//    loop(null, head, listNodeLength-n+1)
//
//
//  }
//}
