package im.xun.zmate.checker

import akka.actor._
import im.xun.zmate.TmsConfigFileUpdate
import scala.concurrent.duration._
import java.io.File
import java.time.LocalDate

object FileMonitorActor {
  def props(filename: String): Props = Props(new FileMonitorActor(filename))
}

class FileMonitorActor(filename: String) extends Actor with ActorLogging {
  import context._

  system.scheduler.scheduleOnce(1 seconds, self, "tick")

  //  override  postRestart  so  we  don’t  call  preStart  and  schedule  a  new  message
  override def postRestart(reason:  Throwable)  =  {}

  var lastModify: Long = 0

  def receive = {
    case "tick" =>
//      log.info("Ticker")
      if(getLastModify > lastModify) {
        log.info("Config file change detected! reload config")
        context.parent ! "FileChanged"
        lastModify = getLastModify
      }
      system.scheduler.scheduleOnce(1 seconds, self, "tick")

  }

  def getLastModify: Long = {
    val f = new File(filename)
    f.lastModified
  }

}
