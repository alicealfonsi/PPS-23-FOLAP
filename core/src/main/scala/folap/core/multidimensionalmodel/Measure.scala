package folap.core.multidimensionalmodel

/** A Measure represents a numeric value associated with an Event
  */
trait Measure:
  /** Abstract type member representing the type of the Measure upper-bounded by
    * MeasureType
    */
  type T <: MeasureType

  /** The name of the Measure
    * @return
    *   the Measure name
    */
  def name: String = getClass.getSimpleName

  /** The value of the Measure
    * @return
    *   the Measure value
    */
  def value: T
