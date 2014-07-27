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

  def receive = {
    case "tick" =>
      log.info("Ticker")
      if(getLastModify > lastModify) {
        log.info("Config file change detected! reload config")
        lastModify = getLastModify
        val tmsDay = TmsChecker.loadConfig()
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
