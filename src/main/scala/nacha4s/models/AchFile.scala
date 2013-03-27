package nacha4s.models

import org.joda.time._

import nacha4s.utils._
import nacha4s.enums._

case class AchFile(
  batches: Iterable[Batch],
  company: Company,
  priorityCode: Byte = 1,
  fileCreationDate: DateTime,
  // This should start at "A" and go up to "Z" (then 0 through 9, this wasn't covered in the docs I read, and doesn't matter now?) to distinguish from other reports made in the same minute.
  fileIdModifier: Char, // UPPERCASE alphanumeric length 1.
  referenceCode: Option[String] // alphanumeric length 8 - for internal accounting
  ) {
  val header = AchFileHeaderRecord(
    company = company,
    priorityCode = priorityCode,
    fileCreationDate = fileCreationDate,
    fileIdModifier = fileIdModifier,
    referenceCode = referenceCode)

  val control = AchFileControlRecord(
    batches = batches.map(_.control))

  // this is the number of lines that should be just 9s to complete the blocks
  val numberOfNineLines: Int = {
    control.totalRecordsNonEmptyInFile % AchConstants.blockingFactor
  }

  val toReportString: String = {
    val sb = StringBuilder.newBuilder
    sb ++= header.toReportString

    for (batch <- batches) { // has side-effects
      sb ++= batch.toReportString
    }
    sb ++= control.toReportString
    sb ++= (("9" * 94) + "\n") * numberOfNineLines
    sb.mkString
  }
}