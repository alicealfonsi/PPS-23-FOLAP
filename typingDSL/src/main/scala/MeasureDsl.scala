package folap.typingDSL

import scala.Numeric

case class Measure[T: Numeric](name: String, typology: String)
