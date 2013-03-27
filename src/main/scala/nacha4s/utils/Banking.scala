package nacha4s.utils

object Banking {
  def isValidRoutingNumber(routingNumber: String): Boolean = {
    // Source: http://en.wikipedia.org/wiki/Routing_transit_number#Check_digit
    val r = String.valueOf(routingNumber).map(_.asDigit)
    def sizeCheck = r.size == 9
    def checkSum = 0 == ( // do the math!
      (3 * (r(0) + r(3) + r(6))) +
      (7 * (r(1) + r(4) + r(7))) +
      (r(2) + r(5) + r(8))
    ) % 10

    sizeCheck && checkSum
  }
}