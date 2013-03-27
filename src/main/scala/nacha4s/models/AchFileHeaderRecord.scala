package nacha4s.models

import org.joda.time._

import nacha4s.utils._
import nacha4s.enums._

case class AchFileHeaderRecord(
  company: Company,
  // all numerics are assumed to be in base-10
  priorityCode: Byte = 1, // numeric length 2
  fileCreationDate: DateTime, // for two fields with formats: date=YYMMDD, time=HHMM 
  // This should start at "A" and go up to "Z" (then 0 through 9, this wasn't covered in the docs I read, and doesn't matter now?) to distinguish from other reports made in the same minute.
  fileIdModifier: Char, // UPPERCASE alphanumeric length 1.
  referenceCode: Option[String] // alphanumeric length 8 - for internal accounting
  ) extends Record {
  val recordTypeCode = 1
  val formatCode = 1

  def fileCreateDateFormatted = AchConstants.dateFormat.print(fileCreationDate)
  def fileCreateTimeFormatted = AchConstants.timeFormat.print(fileCreationDate)

  val toReportString: String = {
    val sb = StringBuilder.newBuilder
    sb ++= f"$recordTypeCode%1d$priorityCode%02d"
    sb ++= f"${company.bankRoutingNumber}%10d${company.irsFederalTaxIdentificationNumber.take(9)}%10s"
    sb ++= f"$fileCreateDateFormatted$fileCreateTimeFormatted"
    sb ++= f"$fileIdModifier${AchConstants.recordSize}%03d${AchConstants.blockingFactor}%02d$formatCode%1d"
    sb ++= f"${company.immediateDestinationName.getOrElse("").take(23)}%-23s"
    sb ++= f"${company.name.take(23)}%-23s"
    sb ++= f"${referenceCode.getOrElse("").take(8)}%-8s"

    val record = sb.mkString
    assert(record.size == AchConstants.recordSize, println(s"Size was ${record.size} but was expected to be ${AchConstants.recordSize}"))

    record + "\n"
  }
}