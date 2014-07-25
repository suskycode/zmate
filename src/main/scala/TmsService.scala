package im.xun.zmate

import akka.actor.SupervisorStrategy.{Restart, Stop}
import akka.actor.{Props, ActorRef, ActorLogging, Actor, OneForOneStrategy, Status}

class TmsService extends Actor with ActorLogging {

  override val supervisorStrategy = OneForOneStrategy() {
    case _: TmsException =>
      log.warning("CheckTms failed, restarting.")
      Restart
    case e =>
      log.error("Unexpected failure: {}", e.getMessage)
      Restart
  }

  val worker = context.actorOf(Props[Worker],"tms-worker")
  val checker = context.actorOf(Props[Checker], "tms-checker")
  var retry = 3;

  def receive = {
    case TmsCheckRequest =>
      log.info("Get TmsCheckRequest Message!")
      retry = 3;
      worker ! TmsCheckRequest

    case TmsCheckSuccess =>
      log.info("Tms Check Sucess!")

    case TmsCheckFail =>
      retry = retry -1;
      if(retry > 0) {
        log.info("Tms Check Fail! Retry {}.", retry)
        worker ! TmsCheckRequest
      }
      if(retry == 0) {
        log.error("TMS check Fail!!!!!!!!!")
      }
  }
}
