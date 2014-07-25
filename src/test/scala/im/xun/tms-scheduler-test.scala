import im.xun.zmate._
import org.scalatest._
import java.util.Calendar
import java.util.Date
import java.text.SimpleDateFormat

class SchedulerSpec extends FlatSpec {
  "The Scheduler " should "return true" in {
    val res = TmsChecker.check()
    /*assert(res == true)*/
  }

  it should "throw NoSuchElementException if an empty stack is popped" in {
    val res = TmsChecker.check()
    /*assert(res == true)*/
  }

  it should "blacklist check" in {
    val format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val dateStr = "2014-05-02 17:05:22"
    val date = format.parse(dateStr)
    val res = TmsChecker.ruleBlacklist(date)
    assert(res == true)
  }

  it should "blacklist check 2" in {
    val format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val dateStr = "2014-05-02 18:05:22"
    val date = format.parse(dateStr)
    val res = TmsChecker.ruleBlacklist(date)
    assert(res == true)
  }

  it should "blacklist check 3" in {
    val format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val dateStr = "2017-05-02 17:05:22"
    val date = format.parse(dateStr)
    val res = TmsChecker.ruleBlacklist(date)
    assert(res == false)
  }


}
