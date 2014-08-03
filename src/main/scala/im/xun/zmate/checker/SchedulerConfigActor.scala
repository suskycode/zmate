package im.xun.zmate.checker

import akka.actor._
import im.xun.zmate.TmsCheckRequest
import scala.concurrent.duration._

class SchedulerConfigActor extends Actor with ActorLogging {
  import context._


  val fileMonitor1 = context.actorOf(FileMonitorActor.props("holiday.txt"), "holiday-monitor")
  val fileMonitor2 = context.actorOf(FileMonitorActor.props("deholiday.txt"), "deholiday-monitor")
  val fileMonitor3 = context.actorOf(FileMonitorActor.props("rulezero.txt"), "rule0-monitor")

  val checkingPool = scala.collection.mutable.ListBuffer.empty[Cancellable]
  def reloadConfig(): Unit = {
    checkingPool.foreach(p => p.cancel())
    checkingPool.clear()
    val tmsCheckPointStream = TmsScheduler.loadConfig()
    tmsCheckPointStream.take(5).foreach(s => println(java.time.LocalDateTime.now.plusSeconds(s)))

    tmsCheckPointStream.take(1).foreach( point =>
      checkingPool += system.scheduler.scheduleOnce(
        point seconds,
        self,
        "check")
    )
  }

  var msgThrottleStamp: Long = 0
  def receive = {
    case "FileChanged" =>
      val ct = System.currentTimeMillis
      if(ct - msgThrottleStamp > 500){
        log.info("Config File changed, reloading!")
        reloadConfig()
      }
      msgThrottleStamp = ct

    case "check" =>
      context.parent ! TmsCheckRequest
      reloadConfig()
  }

}
