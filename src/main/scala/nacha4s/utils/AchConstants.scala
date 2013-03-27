package nacha4s.utils

import org.joda.time.format.DateTimeFormat

object AchConstants {
  val recordSize = 94
  val blockingFactor = 10
  val dateFormat = DateTimeFormat.forPattern("yyMMdd")
  val timeFormat = DateTimeFormat.forPattern("HHmm")
}