package folap.core

import multidimensionalModel._

/** An Event is an instance of a fact that occurred in the business domain
  * @tparam A
  *   the type of Event attributes, which must be a subtype of Attribute
  * @tparam M
  *   the type of Event measures, which must be a subtype of Measure
  */
trait Event[A <: Attribute, M <: Measure]:
  /** The dimensions that describe the Event
    * @return
    *   the list of Event dimensions
    */
  def dimensions: Iterable[A]

  /** The attributes that can be used to describe the Event
    * @return
    *   the list of Event attributes
    */
  final def attributes: Iterable[A] =
    dimensions.flatMap(_.hierarchy)

  /** The measures that quantify the Event
    * @return
    *   the list of Event measures
    */
  def measures: Iterable[M]

object Event:
  extension [A <: Attribute, M <: Measure](event: Event[A, M])
    /** Finds the attributes of this Event whose name is equal to one of the
      * specified ones
      * @param names
      *   the names of the attributes to be found
      * @return
      *   a new iterable collection containing the found attributes
      */
    def findAttributesByNames(names: Iterable[String]): Iterable[A] =
      names.flatMap(name => event.attributes.filter(_.name == name))
