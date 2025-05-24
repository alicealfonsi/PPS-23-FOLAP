package folap.typingDSL

object MeasureDSL:

  extension (t: MeasureType)
    infix def measure(name: String): Measure = Measure(name, t)
