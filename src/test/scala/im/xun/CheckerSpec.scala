import im.xun.zmate._
import akka.testkit.{ImplicitSender, TestKit}
import akka.actor.{Status, Props, ActorSystem}
import org.scalatest.{FlatSpecLike, BeforeAndAfterAll, Matchers}

class CheckerSpec(_system: ActorSystem)
  extends TestKit(_system)
  with FlatSpecLike
  with Matchers
  with BeforeAndAfterAll
  with ImplicitSender {

  def this() = this(ActorSystem("CheckerSpec"))

  override def afterAll: Unit = system.shutdown()

  val timeCheckActor = system.actorOf(Props[Checker],
    "Time-Checker")

  "The Time Checker" should "return ture if now = 50" in {
    timeCheckActor !  "getstatus"
    //expectMsg(true)
  }
}
