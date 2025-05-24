package folap.typingDSL

object MeasureDSL:

  class MeasureName(val name: String)

  extension (space: String)
    infix def measure(name: String): MeasureName = MeasureName(name)

  extension (m: MeasureName)
    infix def as(typology: MeasureType): Measure =
      Measure(m.name, typology)
