package folap.core

import MultidimensionalModel._

/** An Event is an instance of a fact that occurred in the business domain
  * @tparam A
  *   the type of the Event attributes
  * @tparam M
  *   the type of the Event measures
  */
trait Event[L, A <: Attribute[L], M <: Measure]:
  /** The dimensions that describe the Event
    * @return
    *   the list of Event dimensions
    */
  def dimensions: Iterable[A]

  /** The attributes that can be used to describe the Event
    * @return
    *   the list of Event attributes
    */
  def attributes: Iterable[A] =
    dimensions.flatMap(_.hierarchy)

  /** The measures that quantify the Event
    * @return
    *   the list of Event measures
    */
  def measures: Iterable[M]

object Event:
  extension [L, A <: Attribute[L], M <: Measure](event: Event[L, A, M])
    /** Finds the attributes of this Event whose name is equal to one of the
      * specified ones
      * @param names
      *   the names of the attributes to be found
      * @return
      *   a new iterable collection containing the found attributes
      */
    def findAttributesByNames(names: Iterable[L]): Iterable[A] =
      names.flatMap(name => event.attributes.filter(_.name == name))
