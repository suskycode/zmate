package im.xun.zmate

import java.util.Calendar
import java.util.Date
import java.text.SimpleDateFormat

object TmsChecker {
  def check(): Boolean = {
    var now = Calendar.getInstance.getTime
    if(now.getHours == 8 && now.getMinutes == 2 ){
      return true
    }
    return false
  }

  def ruleBlacklist(day: Date): Boolean = {
    val format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val blacklist = scala.io.Source.fromFile("blacklist.txt")
    blacklist.getLines.foreach{s =>
      val time = format.parse(s)
      if(time.getYear == day.getYear &&  time.getMonth == day.getMonth && time.getDate == day.getDate) {
        return true
      }}
      return false
  }
}
