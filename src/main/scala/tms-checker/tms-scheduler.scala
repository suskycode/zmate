package im.xun.zmate

import java.time.LocalDate
import java.time.LocalTime
import java.time.LocalDateTime

object TmsChecker {
  def getCheckPoints(): Stream[Int] = {
    val checkPointAm = LocalTime.parse("08:00")
    val checkPointPm = LocalTime.parse("18:00")

    val now = LocalDateTime.now
    
    workdayStream
  }
  def check(): Boolean = {
//    val now = LocalTime.now
    val checkPointAm = LocalTime.parse("08:00")
    val checkPointPm = LocalTime.parse("18:00")

    false
  }

  def ruleDefault(day: LocalDate): Boolean = {
    val dayOfWeek = day.getDayOfWeek.getValue
    if(dayOfWeek ==  6 || dayOfWeek == 7 ) {
      false
    }
    else {
      true
    }
  }

  def dayInFile(checkDay: LocalDate, dayFile: String): Boolean = {
    var result =false
    val days = scala.io.Source.fromFile(dayFile)
    for(day <- days.getLines()) {
      try{
        val dayInFile = LocalDate.parse(day)
        if(checkDay.compareTo(dayInFile) == 0) {
          result = true
        }
      }
    }
    days.close()

    result
  }

  def ruleHoliday(day: LocalDate): Boolean = {
    dayInFile(day, "holiday.txt")
  }

  def ruleOvertime(day: LocalDate): Boolean = {
    dayInFile(day, "deholiday.txt")
  }

  def ruleAll(day: LocalDate): Boolean = {
    if(ruleHoliday(day)) {
      return false
    }

    if(ruleOvertime(day)) {
      return true
    }

    ruleDefault(day)
  }

  def dayStream = Stream.iterate(LocalDate.now)(_ plusDays 1)

  def workdayStream = dayStream.filter(ruleAll)

  def loadConfig(): Stream[LocalDate] = {
    workdayStream
  }

}

