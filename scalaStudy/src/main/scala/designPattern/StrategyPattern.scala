package designPattern

object StrategyPattern {


  case class Hand(hand: Int) {
    def fight(hand: Hand): Boolean = {
      ???
    }
  }


  // インターフェース
  trait JankenStrategy {

    // 次の手を決める
    def nextHand(hand: Hand): Hand = {
      ???
    }

    // ルールをstringで返す
    def rule: String = {
      ???
    }
  }

  // 具象（実装）

  class normalJankenStrategy extends JankenStrategy {
    override def nextHand(hand: Hand): Hand = ???

    override def rule: String = "勝った方が勝ち"
  }


  class osakaJankenStrategy extends JankenStrategy {
    override def nextHand(hand: Hand): Hand = ???

    override def rule: String = "負けた方が勝ち"
  }

  class atodasiJankenStrategy extends JankenStrategy {
    override def nextHand(hand: Hand): Hand = ???

    override def rule: String = "後出ししてもOK"
  }


  // Player class

  class Player(jankenStrategy: JankenStrategy) {
    def fight(player: Player): Boolean = ???
  }


  // main code
  def main(args: Array[String]): Unit = {
    // strategy patternを使うと、こんな感じでアルゴリズムをごっそり切り替えることができる
    val player1 = new Player(new normalJankenStrategy)
    val player2 = new Player(new osakaJankenStrategy)




  }

}
