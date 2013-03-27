package nacha4s.enums

import nacha4s.utils._

object AchServiceClassType extends Enum {
  sealed trait EnumVal extends Value {
    def code: Int
  }

  val MixedDebitsAndCredits = new EnumVal {
    val name = "MixedDebitsAndCredits"
    val code = 200
  }
  val Credits = new EnumVal {
    val name = "Credits"
    val code = 220
  }
  val Debits = new EnumVal {
    val name = "Debits"
    val code = 225
  }
}