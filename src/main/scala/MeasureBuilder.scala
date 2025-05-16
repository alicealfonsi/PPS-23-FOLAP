object MeasureDSL:

  class MeasureName(val name: String)

  extension (space: String)
    infix def measure(name: String): MeasureName = MeasureName(name)

  extension (m: MeasureName)
    infix def as(typology: String): Measure[_] =
      TypeFromString.resolve(m.name, typology)
