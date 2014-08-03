package im.xun.zmate.checker

import java.time.LocalDate
import java.time.LocalTime
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit.SECONDS

object TmsScheduler {
  def getCheckPoints(dayStream: Stream[LocalDate]): Stream[Long] = {

    val checkPointAm = LocalTime.parse("08:00")
    val checkPointPm = LocalTime.parse("18:00")

    val s1 = dayStream.map(day => getRemainSeconds(day, checkPointAm))
    val s2 = dayStream.map(day => getRemainSeconds(day, checkPointPm))

    val s3 = mergeStreams(s1, s2)
    val s4 = ruleZero.toStream
    val s5 = mergeStreams(s3, s4)

    s5.filter(_ > 0)
  }

  def ruleZero: List[Long] = {
    val checkPoints = getTimeFromFile("rulezero.txt")
    val now = LocalDateTime.now
    checkPoints.map(cp => SECONDS.between(now, cp))
  }

  def getRemainSeconds(day: LocalDate, time: LocalTime): Long = {
    val now = LocalDateTime.now
    val to = LocalDateTime.of(day, time)
    val salt = scala.util.Random.nextLong % 600
    SECONDS.between(now, to) + salt
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

  def getTimeFromFile(file: String): List[LocalDateTime]= {
    val f = scala.io.Source.fromFile(file)
    val res = scala.collection.mutable.ListBuffer.empty[LocalDateTime]
    for(t <- f.getLines()) {
      try{
        LocalDateTime.parse(t) match {
          case time : LocalDateTime =>
            res += time
        }
      }
      catch {
        case e: Exception =>
          println("Rule file datetime format error:" + file + "" + t)
      }
    }
    f.close()
    res.toList
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

  def loadConfig(): Stream[Long] = {
    val workdayStream = dayStream.filter(ruleAll)
    getCheckPoints(workdayStream)
  }

  def mergeStreams(s1: Stream[Long], s2: Stream[Long]):Stream[Long] = {
    if (s1.isEmpty) s2
    else if (s2.isEmpty) s1
    else if (s1.head < s2.head) s1.head #:: mergeStreams(s1.tail, s2)
    else s2.head #:: mergeStreams(s1, s2.tail)
  }
}
