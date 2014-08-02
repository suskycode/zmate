package im.xun.zmate

import akka.actor._
import scala.concurrent.duration._
import java.io.File
import java.time.LocalDate

object ConfigMonitorActor {
  def props(filename: String): Props = Props(new ConfigMonitorActor(filename))
  def scheduleDay(day: LocalDate) = {

  }
}

class ConfigMonitorActor(filename: String) extends Actor with ActorLogging {
  import context._

    system.scheduler.scheduleOnce(1 seconds, self, "tick")

  //  override  postRestart  so  we  don’t  call  preStart  and  schedule  a  new  message
  override def postRestart(reason:  Throwable)  =  {}

  var lastModify: Long = 0
  val checkingPool = scala.collection.mutable.ListBuffer.empty[Cancellable]


  def cancelAll(pool: List[Cancellable]): Unit ={
    pool.foreach(p => p.cancel())
  }

  def receive = {
    case "tick" =>
      log.info("Ticker")
      val a = java.time.LocalTime.now
      println(a)
      if(getLastModify > lastModify) {
        log.info("Config file change detected! reload config")
        context.parent ! TmsConfigFileUpdate
        cancelAll(checkingPool.toList)


        lastModify = getLastModify
        val tmsCheckPointStream = TmsChecker.loadConfig()
        tmsCheckPointStream.take(5).foreach(s => println(java.time.LocalTime.now.plusSeconds(s)))
        val checkPointIterator = tmsCheckPointStream.iterator
        val checkScheduler =
          system.scheduler.scheduleOnce(
            checkPointIterator.next() seconds,
            self,
            "check")
      }
      system.scheduler.scheduleOnce(1 seconds, self, "tick")

    case "check" =>
      context.parent ! TmsCheckRequest

  }

  def getLastModify: Long = {
    val f = new File(filename)
    f.lastModified
  }

  def reloadConfig: Boolean = {
    true
  }

}
