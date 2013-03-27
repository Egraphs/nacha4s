package nacha4s.models

import nacha4s.utils._
import nacha4s.enums._

case class BatchControlRecord(
  serviceClassType: AchServiceClassType.EnumVal,
  entryDetailRecords: Iterable[EntryDetailRecord],
  company: Company,
  batchNumber: Int) extends Record {
  val recordTypeCode = 8
  val entryAndAddendaCount: Int = { // length 6, with leading zeros if necessary
    entryDetailRecords.size
  }
  val entryHash: Long = { // length 10
    entryDetailRecords.map(_.routingNumberWithoutCheckDigit.toLong).sum.toString.take(10).toLong
  }
  val totalDebitEntryDollarAmountInPennies: Long = { // length 12
    entryDetailRecords.map { entry =>
      if (entry.serviceClassType == AchServiceClassType.Debits) {
        entry.amountInPennies
      } else {
        0
      }
    }.sum.toString.take(12).toLong
  }
  val totalCreditEntryDollarAmountInPennies: Long = { // length 12
    entryDetailRecords.map { entry =>
      if (entry.serviceClassType == AchServiceClassType.Credits) {
        entry.amountInPennies
      } else {
        0
      }
    }.sum.toString.take(12).toLong
  }

  val toReportString: String = {
    val sb = StringBuilder.newBuilder
    sb ++= f"$recordTypeCode%1d${serviceClassType.code}%03d"
    sb ++= f"$entryAndAddendaCount%06d$entryHash%010d"
    sb ++= f"$totalDebitEntryDollarAmountInPennies%012d$totalCreditEntryDollarAmountInPennies%012d"
    sb ++= f"${company.irsFederalTaxIdentificationNumber.take(9)}%10s"
    sb ++= " " * 19 // Message Authentication code should be left blank
    sb ++= " " * 6 // Reserved for Federal Reserve use
    sb ++= f"${company.bankRoutingNumberWithoutCheckDigit}%08d"
    sb ++= f"$batchNumber%07d"

    val record = sb.mkString
    assert(record.size == AchConstants.recordSize, println(s"Size was ${record.size} but was expected to be ${AchConstants.recordSize}"))

    record + "\n"
  }
}