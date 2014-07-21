package im.xun.zmate

import akka.actor.SupervisorStrategy.{Restart, Stop}
import akka.actor.{Props, ActorRef, ActorLogging, Actor, OneForOneStrategy, Status}

case object TmsCheckRequest
case object TmsCheckSuccess
case object TmsCheckFail


class TmsService extends Actor with ActorLogging {

  override val supervisorStrategy = OneForOneStrategy() {
    case _: TmsException =>
      log.warning("CheckTms failed, restarting.")
      Restart
    case e =>
      log.error("Unexpected failure: {}", e.getMessage)
      Stop
  }

  val worker = context.actorOf(Props[Worker],"tms-worker")
  val checker = context.actorOf(Props[Checker], "tms-checker")

  def receive = {
    case TmsCheckRequest =>
      log.info("Get TmsCheckRequest Message!")
      worker ! TmsCheckRequest

    case TmsCheckSuccess =>
      log.info("Tms Check Sucess!")

    case TmsCheckFail =>
      log.info("Tms Check Fail!")
      worker ! TmsCheckRequest

  }
}
