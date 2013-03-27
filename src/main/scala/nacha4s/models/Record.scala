package nacha4s.models

import nacha4s.utils._

trait Record {
  def recordTypeCode: Int
  def toReportString: String
}