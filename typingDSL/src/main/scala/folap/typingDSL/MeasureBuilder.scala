package folap.typingDSL

/** A domain-specific language (DSL) to simplify the creation of a measure using
  * a natural, readable syntax*
  */
object MeasureDSL:

  /** Wrapper class representing the name of a measure
    *
    * @param name
    *   name of the measure
    */
  class MeasureName(val name: String)

  /** Helper class used to begin a measure definition in the DSL.
    */
  case class MeasureWord():
    /** Starts the measure definition by specifying its name
      *
      * @param name
      *   The name of the measure
      * @return
      *   A MeasureName instance
      */
    def named(name: String): MeasureName = MeasureName(name)

  /** Entry point to the DSL for creating measures.
    *
    * @return
    *   a MeasureWord instance that allows chaining with `named`
    */
  def measure: MeasureWord = MeasureWord()

  /** Extension method that completes a measure definition by specifying its
    * type.
    *
    * @param m
    *   A MeasureName instance
    * @param typology
    *   The type of the measure as a string ("Int", "Double", "Float", "Long")
    * @return
    *   An Option containing the Measure if the type is recognized; otherwise
    *   None.
    */
  extension (m: MeasureName)
    infix def as(typology: String): Option[Measure] =
      TypeFromString.resolve(m.name, typology)
