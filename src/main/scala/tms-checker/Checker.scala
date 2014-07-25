package im.xun.zmate

import akka.actor._
import akka.actor.SupervisorStrategy.{Escalate, Restart}
import scala.collection.immutable._
import scala.concurrent.duration._
import scala.language.postfixOps

class Checker extends Actor with ActorLogging {
  import context._

  override  def  preStart()  = {
    system.scheduler.scheduleOnce(1 seconds, self, "tick")
  }

  //  override  postRestart  so  we  don’t  call  preStart  and  schedule  a  new  message
  override  def  postRestart(reason:  Throwable)  =  {}

  var res=false
  def receive = {
    case "tick" =>
      /*log.info("TmsChecker tick")*/
      res = TmsChecker.check()
      if(res == true) {
        log.info("TmsChecker positive!, notify Supervisor!")
        res = false
        context.parent ! TmsCheckRequest
      }
      system.scheduler.scheduleOnce(1 seconds, self, "tick")
      /*system.scheduler.scheduleOnce(1 minutes, self, "tick")*/

    case "getstatus" =>
      sender() ! res

    case _ =>
      log.info("Other Message")
  }
}
