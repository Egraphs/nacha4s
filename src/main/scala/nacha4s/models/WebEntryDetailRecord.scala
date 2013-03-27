package nacha4s.models

import nacha4s.enums._
import nacha4s.utils._

case class WebEntryDetailRecord(
  bankAccountType: BankAccountType.EnumVal, // this is for part of field 2
  serviceClassType: AchServiceClassType.EnumVal, // this is for part of field 2
  receivingRoutingNumber: String, // this is fields 3 and 4
  dfiAccountNumber: String, // length 17 numeric, receiver account number, left justify
  amountInPennies: Long,
  individualIdentificationNumber: Option[String] = None, // length 15 alphanumeric
  individualName: String, // length 22 alphanumeric
  paymentType: PaymentType.EnumVal,
  hasAddendaRecord: Boolean = false) extends EntryDetailRecord {
  override val standardEntryClassCode: AchStandardEntryClassCode.EnumVal = AchStandardEntryClassCode.AuthorizationRecievedViaTheInternet
  def transactionCode: Int = {
    val firstDigit = bankAccountType match {
      case BankAccountType.Checking => 2
      case BankAccountType.Savings => 3
      case other => throw new IllegalStateException(s"Did someone add a new BankAccountType recently? Not found = $other")
    }

    val secondDigit = serviceClassType match {
      // I do not handle prenote credits with or without remittance
      case AchServiceClassType.Credits => 2
      case AchServiceClassType.Debits => 7
      case other => throw new IllegalStateException("Did someone add a new AchServiceClassType recently? Not found = $other")
    }

    val ans = (firstDigit * 10) + secondDigit
    assert(ans >= 22 && ans <= 39)
    ans
  }

  def addendaRecordIndicator = if (hasAddendaRecord) 1 else 0

  val toReportString: String = {
    val sb = StringBuilder.newBuilder
    sb ++= f"$recordTypeCode%1d$transactionCode%02d"
    sb ++= f"$receivingRoutingNumber%9s${dfiAccountNumber}%-17s"
    sb ++= f"$amountInPennies%010d"
    sb ++= f"${individualIdentificationNumber.getOrElse("").take(15)}%-15s"
    sb ++= f"${individualName.take(22)}%-22s"
    sb ++= f"${paymentType.code}%-2s$addendaRecordIndicator%1d"
    sb ++= " " * 15 // 15 blank spaces - this is blank for the ACH Operator to use

    val record = sb.mkString
    assert(record.size == AchConstants.recordSize, println(s"Size was ${record.size} but was expected to be ${AchConstants.recordSize}"))

    record + "\n"
  }
}