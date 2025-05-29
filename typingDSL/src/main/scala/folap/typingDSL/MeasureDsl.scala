package folap.typingDSL
/** A class representing a DSL Measure.
  *
  * Each Measure is defined by a name and a typology that restricts it to one of
  * the allowed numeric types defined in MeasureType.
  *
  * @param name
  *   the name of the measure
  *
  * @param typology
  *   the numeric type of the measure (must be one of: Int, Long, Float, Double)
  */
case class Measure(name: String, typology: MeasureType)
