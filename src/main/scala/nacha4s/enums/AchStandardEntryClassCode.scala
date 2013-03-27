package nacha4s.enums

import nacha4s.utils._

object AchStandardEntryClassCode extends Enum {
  sealed trait EnumVal extends Value {
    def code: String
  }

  val PrearrangedPaymentsAndDepositEntries = new EnumVal {
    val name = "PrearrangedPaymentsAndDepositEntries"
    val code = "PPD"
  }
  val CashConcentrationAndDisbursementEntries = new EnumVal {
    val name = "CashConcentrationAndDisbursementEntries"
    val code = "CCD"
  }
  val CorporateTradeExchangeEntries = new EnumVal {
    val name = "CorporateTradeExchangeEntries"
    val code = "CTX"
  }
  val TelephoneInitiatedEntries = new EnumVal {
    val name = "TelephoneInitiatedEntries"
    val code = "TEL"
  }
  val AuthorizationRecievedViaTheInternet = new EnumVal {
    val name = "AuthorizationRecievedViaTheInternet"
    val code = "WEB"
  }
}