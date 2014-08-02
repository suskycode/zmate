package im.xun.zmate

import java.time.LocalDate
import java.time.LocalTime
import java.time.LocalDateTime

object TmsChecker {
  def getCheckPoints(dayStream: Stream[LocalDate]): Stream[Int] = {


    val checkPointAm = LocalTime.parse("08:00")
    val checkPointPm = LocalTime.parse("18:00")

    val s1 = dayStream.map(day => getRemainSeconds(day, checkPointAm))
    val s2 = dayStream.map(day => getRemainSeconds(day, checkPointPm))

    dropZeroHead(mergeStream(s1, s2))
  }

  def dropZeroHead(s: Stream[Int]): Stream[Int] = {
    if(s.head == 0)
      dropZeroHead(s.tail)
    else
      s
  }

  def getRemainSeconds(day: LocalDate, time: LocalTime): Int = {
    val now = LocalDateTime.now
    val to = LocalDateTime.of(day, time)
    if(to.compareTo(now) < 1) {
      0
    }
    else {
      getSecondsOfYear(to) - getSecondsOfYear(now);
    }

  }

  def getSecondsOfYear(dt: LocalDateTime) = {
    dt.getDayOfYear * 24 * 60 * 60 + dt.getHour * 60 * 60 + dt.getMinute * 60 + dt.getSecond
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


  def loadConfig(): Stream[Int] = {
    val workdayStream = dayStream.filter(ruleAll)
    getCheckPoints(workdayStream)
  }

  def mergeStream(s1: Stream[Int], s2: Stream[Int]):Stream[Int] = {
    s1.head #:: s2.head #:: mergeStream(s1.tail, s2.tail)
  }

}

