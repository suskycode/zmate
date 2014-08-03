package im.xun.zmate.checker

import akka.actor._
//import akka.actor.SupervisorStrategy.{Escalate, Restart}
import im.xun.zmate.{TmsCheckRequest, TmsConfigFileUpdate}
//import scala.collection.immutable._
//import scala.concurrent.duration._
import scala.language.postfixOps

class Checker extends Actor with ActorLogging {

  val configMonitorActor = context.actorOf(Props[SchedulerConfigActor], "scheduler-config")

  def receive = {
    case "check" =>
      context.parent ! TmsCheckRequest

    case _ =>
      log.info("Other Message")
  }
}
