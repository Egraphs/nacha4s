package nacha4s.models

import nacha4s.utils._
import nacha4s.enums._

abstract class EntryDetailRecord extends Record {
  val recordTypeCode = 6
  def serviceClassType: AchServiceClassType.EnumVal
  def standardEntryClassCode: AchStandardEntryClassCode.EnumVal
  def amountInPennies: Long // length 10 numeric
  def receivingRoutingNumber: String // this must be length 9 and validate
  def routingNumberWithoutCheckDigit = receivingRoutingNumber.toInt / 10

  assert(Banking.isValidRoutingNumber(receivingRoutingNumber))
}