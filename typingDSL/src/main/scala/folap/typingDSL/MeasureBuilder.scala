package folap.typingDSL

object MeasureDSL:

  class MeasureName(val name: String)

  case class MeasureWord():
    def named(name: String): MeasureName = MeasureName(name)


  def measure: MeasureWord = MeasureWord()

  extension (m: MeasureName)
    infix def as(typology: String): Option[Measure] =
      TypeFromString.resolve(m.name, typology)

      
