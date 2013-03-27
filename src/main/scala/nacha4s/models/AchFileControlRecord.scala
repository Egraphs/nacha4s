package nacha4s.models

import nacha4s.utils._
import nacha4s.enums._

case class AchFileControlRecord(
  batches: Iterable[BatchControlRecord]) extends Record {
  val recordTypeCode = 9
  val batchCount = batches.size
  val totalRecordsNonEmptyInFile: Int = {
    val batchHeaderAndControlRecords = batches.size * 2
    val fileHeaderAndControlRecords = 2
    entryAndAddendaCount + batchHeaderAndControlRecords + fileHeaderAndControlRecords
  }
  val blockCount: Int = {
    if (totalRecordsNonEmptyInFile % AchConstants.blockingFactor == 0) {
      totalRecordsNonEmptyInFile / AchConstants.blockingFactor
    } else {
      (totalRecordsNonEmptyInFile / AchConstants.blockingFactor) + 1
    }
  }

  def entryAndAddendaCount = {
    batches.map(_.entryAndAddendaCount).sum
  }
  def entryHash: Long = { // length 10
    batches.map(_.entryHash).sum.toString.take(10).toLong
  }
  def totalDebitEntryDollarAmountInPennies: Long = { // length 12
    batches.map(_.totalDebitEntryDollarAmountInPennies).sum.toString.take(12).toLong
  }
  def totalCreditEntryDollarAmountInPennies: Long = { // length 12
    batches.map(_.totalCreditEntryDollarAmountInPennies).sum.toString.take(12).toLong
  }

  val toReportString: String = {
    val sb = StringBuilder.newBuilder
    sb ++= f"$recordTypeCode%1d$batchCount%06d$blockCount%06d"
    sb ++= f"$entryAndAddendaCount%08d$entryHash%010d"
    sb ++= f"$totalDebitEntryDollarAmountInPennies%012d$totalCreditEntryDollarAmountInPennies%012d"
    sb ++= " " * 39 // Reserved

    val record = sb.mkString
    assert(record.size == AchConstants.recordSize, println(s"Size was ${record.size} but was expected to be ${AchConstants.recordSize}"))

    record + "\n"
  }
}