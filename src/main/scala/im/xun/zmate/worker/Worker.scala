package im.xun.zmate.worker

import akka.actor._
//import akka.actor.SupervisorStrategy.{Escalate, Restart}
import im.xun.zmate.{TmsCheckSuccess, TmsCheckFail, TmsCheckRequest}
//import scala.collection.immutable._
//import scala.concurrent.forkjoin.ThreadLocalRandom


class Worker extends Actor with ActorLogging {
	val conf = com.typesafe.config.ConfigFactory.load("tms.conf")
  var ie_cache_dir = conf.getString("tms.ie_cache_dir")

  def receive = {
    case TmsCheckRequest =>
      log.info("Get Tms Check Request Message!")
      /*flakiness*/
      val result = TmsRun.run(ie_cache_dir)
      if(result)
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

}
