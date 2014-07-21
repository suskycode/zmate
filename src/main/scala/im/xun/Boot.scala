package im.xun.zmate

import akka.actor.{ActorSystem, Props}

object Boot extends App{
  val system = ActorSystem("tms-system")
  val tmsService = system.actorOf(Props[TmsService], "tms-service")

  system.registerOnTermination {
    system.log.info("TMS system shutdown.")
  }

}
