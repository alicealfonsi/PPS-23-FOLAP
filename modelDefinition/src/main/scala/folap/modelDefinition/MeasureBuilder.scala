package folap.modelDefinition

/** A domain-specific language (DSL) for creating Measures
  *
  * This DSL enforces type safety by accepting only specific numeric types
  * defined in MeasureType.
  */
object MeasureDSL:
  /** Intermediate representation of a measure's name.
    *
    * @param name
    *   the name assigned to the measure
    */
  class MeasureName(val name: String)

  /** Class that provides the `named` infix method to specify a measure name.
    */
  case class MeasureWord():
    /** Associates a name to a Measure.
      *
      * @param name
      *   the name of the measure
      * @return
      *   a MeasureName wrapping the provided name
      */
    infix def named(name: String): MeasureName = MeasureName(name)

  /** DSL initializer.
    *
    * Starts the construction of a Measure using the
    * `measure named "..." as Type` syntax.
    *
    * @return
    *   a MeasureWord instance
    */
  def measure = MeasureWord()

  /** Extension method for Measurename to complete a Measure definition by
    * assigning its typology.
    *
    * @param typology
    *   the static type of the measure (e.g., Int, Long, Float, Double)
    * @return
    *   a Measure instance with the specified name and typology
    */
  extension (m: MeasureName)
    infix def as(typology: MeasureType): Measure =
      Measure(m.name, typology)
