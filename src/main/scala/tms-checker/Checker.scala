package im.xun.zmate

import akka.actor._
import akka.actor.SupervisorStrategy.{Escalate, Restart}
import scala.collection.immutable._
import scala.concurrent.duration._
import scala.language.postfixOps

class Checker extends Actor with ActorLogging {

  val configMonitorActor = context.actorOf(ConfigMonitorActor.props("holiday.txt"), "tms-conf-monitor")
  val configMonitorActor = context.actorOf(ConfigMonitorActor.props("deholiday.txt"), "tms-conf-monitor")

  def receive = {
    case "tick" =>
      /*log.info("TmsChecker tick")*/

    case "getstatus" =>
//      sender() ! res

    case _ =>
      log.info("Other Message")
  }
}
