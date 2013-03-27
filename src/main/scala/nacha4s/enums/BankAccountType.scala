package nacha4s.enums

import nacha4s.utils._

/**
 * Enum for describing an the type of a bank account.
 */
object BankAccountType extends Enum {
  sealed trait EnumVal extends Value

  val Checking = new EnumVal {
    val name = "Checking"
  }
  val Savings = new EnumVal {
    val name = "Savings"
  }
}