object Test{
  def compileTests() = {
    for (i <- 0 to 10) {
      sampleLoop(i)
    }
  }

  def sampleLoop(num: Int) = {
//    println(s"loopppp${num}")
  }

  println(compileTests())
}
