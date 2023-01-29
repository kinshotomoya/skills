package detastructure

import scala.annotation.tailrec

object LinkedHashMap {

  class LinkedHashMap {
    val ARRAY_LENGTH = 16
    val array = new Array[DoublyLinkedList](ARRAY_LENGTH)
    // 最後にarrayに追加されたlastDoublyLinkedList
    var lastDoublyLinkedList: Option[DoublyLinkedList] = None
    var size: Int = 0

    def put(key: String, value: String): Unit = {
      val arrayIndex = doHash(key)
      val newDoublyLinkedList = DoublyLinkedList.createFistDoublyLinkedList(key, value)
      size += 1
      // Optionで囲ってあげないとnullが返ってくる可能性あり
      Option(array(arrayIndex)) match {
        case Some(value: DoublyLinkedList) =>
          val sameIndexLastDoublyLinkedList = findSameIndexLastDoublyLinkedList(value)
          sameIndexLastDoublyLinkedList.next = Some(newDoublyLinkedList)
          newDoublyLinkedList.before = Some(sameIndexLastDoublyLinkedList)
        case None =>
          this.lastDoublyLinkedList match {
            case Some(value) =>
              value.after = Some(newDoublyLinkedList)
              newDoublyLinkedList.before = Some(value)
            case None =>
          }
      }
      this.lastDoublyLinkedList = Some(newDoublyLinkedList)
    }

    // valueのnextを辿っていき最後のDoublyLinkedListを返す
    // nextがない場合は受け取ったvalueを返す
    private def findSameIndexLastDoublyLinkedList(value: DoublyLinkedList): DoublyLinkedList = {
      @tailrec
      def loop(item: DoublyLinkedList, nextItem: Option[DoublyLinkedList]): DoublyLinkedList = {
        nextItem match {
          case None => item
          case Some(item) => loop(item, item.next)
        }
      }
      loop(value, value.next)
    }

    // 自前のhash関数
    private def doHash(key: String): Int = {
      key.length % ARRAY_LENGTH
    }

  }

  object DoublyLinkedList {
    def createFistDoublyLinkedList(key: String, value: String): DoublyLinkedList = {
      new DoublyLinkedList(
        before = None,
        after = None,
        next = None,
        key = key,
        value = value
      )
    }
  }

  class DoublyLinkedList(var before: Option[DoublyLinkedList],var after:Option[DoublyLinkedList],var next: Option[DoublyLinkedList],val key: String,val value: String) {
  }


  def main(args: Array[String]): Unit = {
    val map = new LinkedHashMap()
    map.put("kinsho", "奈良県")
    map.put("kinsho", "東京都")
    map.put("siota", "大阪府")
    println(map.size)
    println(map.lastDoublyLinkedList.get.before.get.value)
    println(map.lastDoublyLinkedList.get.after)
    println(map.lastDoublyLinkedList.get.next)
    println(map.lastDoublyLinkedList.get.key)
    println(map.lastDoublyLinkedList.get.value)
  }




}
