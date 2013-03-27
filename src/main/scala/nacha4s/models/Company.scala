package nacha4s.models

import nacha4s.utils._
import nacha4s.enums._

case class Company(
  name: String,
  irsFederalTaxIdentificationNumber: String,
  bankRoutingNumber: Int,
  immediateDestinationName: Option[String]) {
  assert(name.size <= 16, println(s"Company name = $name is too long, must be less than 16."))
  assert(irsFederalTaxIdentificationNumber.size <= 9, println(s"IRS Federtal Tax Identification Number = $irsFederalTaxIdentificationNumber is too long, must be less than 10."))
  assert(bankRoutingNumber.toString.size == 9, println(s"Bank routing number must be length = 8 not $bankRoutingNumber."))

  def bankRoutingNumberWithoutCheckDigit: Int = bankRoutingNumber / 10
}