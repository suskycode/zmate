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

  def ruleDefault(day: Date): Boolean = {
    if(day.getDay==6 || day.getDay==7) {
      return false
    }
    return true
  }

  def ruleBlacklist(day: Date): Boolean = {
    val format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val blacklist = scala.io.Source.fromFile("blacklist.txt")
    blacklist.getLines.foreach{s =>
      val time = format.parse(s)
      if(time.getYear == day.getYear &&  time.getMonth == day.getMonth && time.getDate == day.getDate) {
        return false
      }}
      return true
  }

  def ruleWhitelist(day: Date): Boolean = {
    val format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val blacklist = scala.io.Source.fromFile("whitelist.txt")
    blacklist.getLines.foreach{s =>
      val time = format.parse(s)
      if(time.getYear == day.getYear &&  time.getMonth == day.getMonth && time.getDate == day.getDate) {
        return true
      }}
      return false
  }

}

/* JAVA8
import java.time.LocalDate
import java.time.LocalTime

val time =  LocalTime.now
val date = LocalDate.now
val date = LocalDate.of(2014, 8, 1)
val date = LocalDate.parse("2014-01-02")*/
