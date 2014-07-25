package im.xun.zmate

import akka.actor._
import akka.actor.SupervisorStrategy.{Escalate, Restart}
import scala.collection.immutable._
import scala.concurrent.forkjoin.ThreadLocalRandom

class TmsException extends Exception("Flakiness")

class Worker extends Actor with ActorLogging {
	val conf = com.typesafe.config.ConfigFactory.load("tms.conf")
  var ie_cache_dir = conf.getString("tms.ie_cache_dir")

  def receive = {
    case TmsCheckRequest =>
      log.info("Get Tms Check Request Message!")
      /*flakiness*/
      val result = TmsRun.run(ie_cache_dir)
      if(result == true)
        context.parent ! TmsCheckSuccess
      else
        context.parent ! TmsCheckFail
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
