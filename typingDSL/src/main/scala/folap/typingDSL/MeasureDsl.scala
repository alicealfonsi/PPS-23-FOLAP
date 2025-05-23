package folap.typingDSL

/** Represents a measure in the DSL.
  * @param name
  *   name of the measure, expressed as a string
  * @param typology
  *   type of the measure ("Int", "Double", "Float", "Long"), expressed as a
  *   string
  */
case class Measure(name: String, typology: String)
