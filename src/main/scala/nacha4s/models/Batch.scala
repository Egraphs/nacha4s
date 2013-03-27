package nacha4s.models

import org.joda.time._

import nacha4s.utils._
import nacha4s.enums._

case class Batch(
  entryDetailRecords: Iterable[EntryDetailRecord],
  company: Company,
  serviceClassType: AchServiceClassType.EnumVal,
  companyDiscretionaryData: Option[String] = None,
  companyEntryDescription: String,
  companyDescriptiveDate: Option[DateTime] = None,
  effectiveEntryDate: DateTime,
  batchNumber: Int) {
  val standardEntryClassCodes = entryDetailRecords.map(_.standardEntryClassCode).toSet
  assert(standardEntryClassCodes.size == 1, println(s"There should be exactly 1 type of entry per batch, but entry types were: $standardEntryClassCodes"))
  val standardEntryClassCode = standardEntryClassCodes.head

  val header = BatchHeaderRecord(
    company = company,
    serviceClassType = serviceClassType,
    companyDiscretionaryData = companyDiscretionaryData,
    standardEntryClassCode = standardEntryClassCode,
    companyEntryDescription = companyEntryDescription,
    companyDescriptiveDate = companyDescriptiveDate,
    effectiveEntryDate = effectiveEntryDate,
    batchNumber = batchNumber)

  val control = BatchControlRecord(
    serviceClassType = serviceClassType,
    entryDetailRecords = entryDetailRecords,
    company = company,
    batchNumber = batchNumber)

  val toReportString: String = {
    val sb = StringBuilder.newBuilder
    sb ++= header.toReportString

    for (record <- entryDetailRecords) { // has side-effects
      sb ++= record.toReportString
    }
    sb ++= control.toReportString
    sb.mkString
  }
}