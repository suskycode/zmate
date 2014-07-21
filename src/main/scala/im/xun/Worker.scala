package im.xun.zmate

import akka.actor._
import akka.actor.SupervisorStrategy.{Escalate, Restart}
import scala.collection.immutable._
import scala.concurrent.forkjoin.ThreadLocalRandom

class TmsException extends Exception("Flakiness")

class Worker extends Actor with ActorLogging {
  def receive = {
    case TmsCheckRequest =>
      log.info("Get Tms Check Request Message!")
      /*flakiness*/
      context.parent ! TmsCheckSuccess
    case _ =>
      log.info("Other Message")
  }

  override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
    log.info("preRestart!")
    context.parent ! TmsCheckFail
  }

  private def flakiness(): Unit =
      throw new TmsException
}
