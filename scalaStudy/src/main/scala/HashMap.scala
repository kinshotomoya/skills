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

}
