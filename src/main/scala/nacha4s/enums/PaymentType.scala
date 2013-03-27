package nacha4s.enums

import nacha4s.utils._

object PaymentType extends Enum {
  sealed trait EnumVal extends Value {
    def code: Char
  }

  val Reccuring = new EnumVal {
    val name = "Reccuring"
    val code = 'R'
  }
  val SingleEntry = new EnumVal {
    val name = "SingleEntry"
    val code = 'T'
  }
}