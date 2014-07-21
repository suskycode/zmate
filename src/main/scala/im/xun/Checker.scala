package im.xun.zmate

import akka.actor._
import akka.actor.SupervisorStrategy.{Escalate, Restart}
import scala.collection.immutable._

class Checker extends Actor with ActorLogging {
  def receive = {
    case _ =>
      log.info("Other Message")
  }

  override def preStart(): Unit = {
    log.info("preStart")
    context.parent ! TmsCheckRequest
  }
}
