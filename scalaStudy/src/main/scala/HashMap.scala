import scala.collection.mutable.ArrayBuffer

object HashMap {
  class HashMap {

    val N = 1000
    val hashtable = new Array[scala.collection.mutable.ArrayBuffer[(Int, Int)]](N)


    def get(key: Int): Int = {
      val bucketCode = key % N

      (for {
        bucket <- Option(hashtable(bucketCode))
        value <- bucket.collectFirst{
          case (k, v) if k == key => v
        }
      } yield value).getOrElse(-1)
    }


    def remove(key: Int): Unit = {
      val bucketCode = key % N
      Option(hashtable(bucketCode)) match {
        case Some(bucket) =>
          val index = bucket.indexWhere {
            case (k, _) => k == key
          }
          if(index != -1) bucket.remove(index)
        case None =>
      }
    }

    def put(key: Int, value: Int): Unit = {
      val bucketCode = key % N
      Option(hashtable(bucketCode)) match {
        case None => {
          val bucket = new collection.mutable.ArrayBuffer[(Int, Int)]()
          hashtable.update(bucketCode, bucket.append((key, value)))
        }
        case Some(bucket) => {
          val index = bucket.indexWhere{
            case (k, _) => k == key
          }
          if(index != -1) bucket.update(index, (key, value)) else bucket.append((key, value))
        }
      }
    }

  }


  class HashMapWithList {
    val N = 1000
    // 衝突が起きた場合は、同じスロット内でLinkedList（次要素のポインタを持つデータ構造）を作って、
    // 同じスロット内にデータを格納していく
    val hashTable = new Array[scala.collection.mutable.ListBuffer[(Int, Int)]](N)

    def get(key: Int): Int = {
      val bucketCode = key % 1000

      (for {
        bucket <- Option(hashTable(bucketCode))
        targetElement <- bucket.find{
          case (k, _) => k == key
        }
      } yield targetElement._2).getOrElse(-1)
    }


    def remove(key: Int): Unit = {
      val bucketCode = key % N
      Option(hashTable(bucketCode)) match {
        case Some(bucket) =>
          val index = bucket.indexWhere{
            case (k, _) => k == key
          }
          // LinkedListのremoveの計算量はO(1)だが、indexを指定の削除の場合は
          // その対象indexまで行かないといけないから、O(n)の計算量になる
          if(index != -1) bucket.remove(index)
        case None =>
      }
    }

    def put(key: Int, value: Int): Unit = {
      val bucketCode = key % N
      Option(hashTable(bucketCode)) match {
        case None => {
          val newList = new scala.collection.mutable.ListBuffer[(Int, Int)]()
          hashTable.update(bucketCode, newList.append((key, value)))
        }
        case Some(bucket) => {
          val index = bucket.indexWhere{
            case (k, _) => k == key
          }
          if(index != -1) bucket.update(index, (key, value)) else bucket.append((key, value))
        }
      }
    }

  }

  class HashMapWithNode {
    val N = 100

    class Node(var key: Int, var value: Int, var previous: Node, var next: Node)

    val hashTable = new Array[Node](N)


    def get(key: Int): Int = {
      def loop(next: Node): Int = {
        if(next == null) {
          -1
        } else {
          if(next.key == key) {
            next.value
          } else {
            loop(next.next)
          }
        }
      }
      val hashCode = key % N
      val node = hashTable(hashCode)
      loop(node)
    }


    def remove(key: Int): Unit = {
      val hashCode = key % N
      val node = hashTable(hashCode)

      def loop(next: Node): Unit = {
        if(next != null) {
          if(next.key == key) {
            if(next.previous == null) {
              hashTable.update(hashCode, null)
            } else {
              next.previous.next = next.next
            }
          } else {
            loop(node.next)
          }
        } else {

        }
      }
      loop(node)
    }


    def put(key: Int, value: Int): Unit = {
      val hashCode = key % N
      val node = hashTable(hashCode)

      def loop(next: Node): Unit = {
        if(next == null) {
          val newNode = new Node(key, value, null, null)
          hashTable.update(hashCode, newNode)
        } else {
          if(next.key == key) {
            next.value = value
          } else {
            if(next.next == null) {
              next.next = new Node(key = key, value = value, previous = next, next = null)
            } else {
              loop(next.next)
            }
          }
        }
      }
      loop(node)
    }

  }



  class HashMapWithNoneNode {
    val N = 100

    class Node(var key: Int, var value: Int, var next: Option[Node])

    object Node {
      def empty = new Node(-1, -1, None)
    }

    val hashTable = ArrayBuffer.fill(N)(Node.empty)

    def get(key: Int): Int = {
      def loop(target: Option[Node]): Int = {
        target match {
          case None => -1
          case Some(node) => {
            if(node.key == key) {
              node.value
            } else {
              loop(node.next)
            }
          }
        }
      }

      val hashCode = key % N
      val node = Option(hashTable(hashCode))
      loop(node)
    }


    def remove(key: Int): Unit = {
      val hashCode = key % N
      val previous = Option(hashTable(hashCode))
      val node = previous

      def loop(previous: Option[Node], target: Option[Node]): Unit = {

        target match {
          case None =>
          case Some(node) => {
            if(node.key == key) {
              previous match {
                case None =>
                case Some(pre) => {
                  pre.next = node.next
                }
              }
            } else {
              loop(Some(node), node.next)
            }
          }
        }
      }
      loop(previous, node)
    }


    def put(key: Int, value: Int): Unit = {
      val hashCode = key % N
      val previous = Option(hashTable(hashCode))
      val node = previous


      def loop(previous: Option[Node], target: Option[Node]): Unit = {

        (previous, target) match {
          case (Some(pre), None) => {
            pre.next = Some(new Node(key, value, None))
          }
          case (Some(_), Some(node)) => {
            if(node.key == key) {
              node.value = value
            } else {
              node.next match {
                case None => {
                  node.next = Some(new Node(key, value, None))
                }
                case Some(_) => loop(Some(node), node.next)
              }
            }
          }
          case _ =>
        }
      }
      loop(previous, node)
    }

  }


}
