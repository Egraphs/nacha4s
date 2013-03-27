package nacha4s.models

import org.joda.time._

import nacha4s.utils._
import nacha4s.enums._

case class BatchHeaderRecord(
  company: Company,
  serviceClassType: AchServiceClassType.EnumVal,
  companyDiscretionaryData: Option[String] = None, // length 20 - this probably is always blank, it is just for internal use
  standardEntryClassCode: AchStandardEntryClassCode.EnumVal, // length 3
  companyEntryDescription: String, // length 10
  companyDescriptiveDate: Option[DateTime] = None, // alphanumeric length 6 - this could be the date they earned the income for example
  effectiveEntryDate: DateTime, // YYMMDD - date transactions are to be posted to participants' account
  batchNumber: Int // length 7, sequential (starting at 1? not in examples)
  ) extends Record {
  val recordTypeCode = 5
  val originatorStatusCode = 1
  val originatingFinancialInstitution: Int = company.bankRoutingNumberWithoutCheckDigit // length 8, routing number without the last digit

  val toReportString: String = {
    val sb = StringBuilder.newBuilder
    sb ++= f"$recordTypeCode%1d${serviceClassType.code}%03d"
    sb ++= f"${company.name.take(16)}%-16s${companyDiscretionaryData.getOrElse("").take(20)}%-20s"
    sb ++= f"${company.irsFederalTaxIdentificationNumber.take(9)}%10s"
    sb ++= f"${standardEntryClassCode.code}%3s${companyEntryDescription.take(10)}%-10s"
    sb ++= f"${companyDescriptiveDate.map(AchConstants.dateFormat.print(_)).getOrElse("")}%6s"
    sb ++= f"${AchConstants.dateFormat.print(effectiveEntryDate)}%6s"
    sb ++= "   " // three blank spaces - this is blank for the ACH Operator to use
    sb ++= f"$originatorStatusCode%1d$originatingFinancialInstitution%08d"
    sb ++= f"$batchNumber%07d"

    val record = sb.mkString
    assert(record.size == AchConstants.recordSize, println(s"Size was ${record.size} but was expected to be ${AchConstants.recordSize}"))

    record + "\n"
  }
}