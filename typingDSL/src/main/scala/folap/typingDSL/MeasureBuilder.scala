package folap.typingDSL
package folap.typingDSL
object MeasureDSL:

  class MeasureName(val name: String)

  case class MeasureWord():
    infix def named(name: String): MeasureName = MeasureName(name)

  def measure = MeasureWord()

  extension (m: MeasureName)
    infix def as(typology: MeasureType): Measure =
      Measure(m.name, typology)
