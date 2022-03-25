package zios

import zio.clock.Clock
import zio.{Runtime, ZIO}

import java.time.Duration

object Zios extends App {

  def get(): ZIO[Clock, Nothing, String] = {
    println(s"get-${Thread.currentThread().getName}-${Thread.currentThread().getId}")
    for {
      _ <- ZIO.sleep(Duration.ofMillis(3000))
      _ <- ZIO.succeed(println("get"))
      g <- ZIO.succeed("get")
    } yield g
  }

  def set(): ZIO[Any, Nothing, String] = {
    println(s"set-${Thread.currentThread().getName}-${Thread.currentThread().getId}")
    ZIO.succeed("set")
  }

  val program: ZIO[Clock, Nothing, Unit] = for {
    _ <- get().forkDaemon
    s <- set()
  } yield {
    get().forkDaemon
    set()
    println(s)
  }

  Runtime.default.unsafeRunToFuture(program)

  println("finish")

}
