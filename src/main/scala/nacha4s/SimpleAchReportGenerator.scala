package nacha4s

import org.joda.time._
import org.joda.time.format.DateTimeFormat

import nacha4s.utils._
import nacha4s.enums._
import nacha4s.models._

// Generate the NACHA file using a single batch record
case class SimpleAchReportGenerator(
  company: Company,
  reportDate: DateTime,
  paymentDate: DateTime,
  entryDetailRecords: Iterable[EntryDetailRecord],
  reportType: AchServiceClassType.EnumVal,
  fileIdModifier: Char = 'A'
) {
  def generate: String = {
    val reportDate = new DateTime
    val referenceCodeDateFormatter = DateTimeFormat.forPattern("MMMyy")
    val referenceCode = reportDate.toString(referenceCodeDateFormatter) + reportType.name.take(3)

    val batch = Batch(
      entryDetailRecords = entryDetailRecords,
      company = company,
      serviceClassType = reportType,
      companyEntryDescription = referenceCode,
      companyDescriptiveDate = Some(reportDate),
      effectiveEntryDate = paymentDate,
      batchNumber = 1
    )

    val header = AchFile(
      batches = List(batch),
      company = company,
      fileCreationDate = reportDate,
      fileIdModifier = fileIdModifier,
      referenceCode = Some(referenceCode)
    )

    val report = StringBuilder.newBuilder
    report ++= header.toReportString
    report.mkString
  }
}