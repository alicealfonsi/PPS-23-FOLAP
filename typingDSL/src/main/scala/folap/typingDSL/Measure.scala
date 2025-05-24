package folap.typingDSL

type MeasureType = Int.type | Long.type | Float.type | Double.type

case class Measure(name: String, typology: MeasureType)
