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
          sameIndexLastDoublyLinkedList.sameIndexAfter = Some(newDoublyLinkedList)
          newDoublyLinkedList.sameIndexBefore = Some(sameIndexLastDoublyLinkedList)
          newDoublyLinkedList.before = Some(sameIndexLastDoublyLinkedList)
        case None =>
          array.update(arrayIndex, newDoublyLinkedList)
          this.lastDoublyLinkedList match {
            case Some(value) =>
              value.after = Some(newDoublyLinkedList)
              newDoublyLinkedList.before = Some(value)
            case None =>
          }
      }
      this.lastDoublyLinkedList = Some(newDoublyLinkedList)
    }


    def remove(key: String): Unit = {
      val arrayIndex = doHash(key)
      Option(array(arrayIndex)) match {
        case None => // array自体に指定されたkeyのindexが存在しない場合は何もしない
        case Some(value: DoublyLinkedList) =>
          size -= 1
          if(array(arrayIndex) == value) {
            array.update(arrayIndex, value.sameIndexAfter.orNull)
          }
          findTargetDoublyLinkedList(value, key)
      }
    }

    private def findTargetDoublyLinkedList(value: DoublyLinkedList, key: String): Unit = {
      @tailrec
      def loop(item: DoublyLinkedList, beforeItem: Option[DoublyLinkedList], afterItem: Option[DoublyLinkedList]): Unit = {
        if (item.value == key) {
          beforeItem.foreach(_.after = afterItem)
          afterItem.foreach(_.before = beforeItem)
          item.sameIndexBefore.foreach(_.sameIndexAfter = item.sameIndexAfter)
          item.sameIndexAfter.foreach(_.sameIndexBefore = item.sameIndexBefore)
        } else {
          afterItem match {
            case None => // ない場合は何もしない
            case Some(newItem) => loop(newItem, newItem.sameIndexBefore, newItem.sameIndexAfter)
          }
        }
      }

      loop(value, value.sameIndexBefore, value.sameIndexAfter)
    }

    // valueのnextを辿っていき最後のDoublyLinkedListを返す
    // nextがない場合は受け取ったvalueを返す
    private def findSameIndexLastDoublyLinkedList(value: DoublyLinkedList): DoublyLinkedList = {
      @tailrec
      def loop(item: DoublyLinkedList, nextItem: Option[DoublyLinkedList]): DoublyLinkedList = {
        nextItem match {
          case None => item
          case Some(item) => loop(item, item.sameIndexAfter)
        }
      }
      loop(value, value.sameIndexAfter)
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
        sameIndexAfter = None,
        sameIndexBefore = None,
        key = key,
        value = value
      )
    }
  }

  class DoublyLinkedList(var before: Option[DoublyLinkedList], var after:Option[DoublyLinkedList], var sameIndexAfter: Option[DoublyLinkedList], var sameIndexBefore: Option[DoublyLinkedList], val key: String, val value: String) {
  }


  def main(args: Array[String]): Unit = {
    val map = new LinkedHashMap()
    map.put("kinsho", "奈良県")
    map.put("kinsho", "神奈川県")
    map.put("kinsho", "東京都")
    map.put("kinsho", "埼玉県")
    map.put("siota", "大阪府")
    map.remove("kinsho")
    println(map.array(6).value)
    println(map.array(6).sameIndexAfter.get.value)
    println(map.array(6).sameIndexAfter.get.sameIndexBefore.get.value)
    println(map.array(6).sameIndexAfter.get.sameIndexAfter.get.value)
    println(map.array(6).sameIndexAfter.get.sameIndexAfter.get.sameIndexAfter) // None
  }




}
