package folap.core

import MultidimensionalModel._

/** An Event is an instance of a fact that occurred in the business domain
  * @tparam A
  *   the type of the Event attributes
  * @tparam M
  *   the type of the Event measures
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
  def attributes: Iterable[A] =
    dimensions.flatMap(_.hierarchy)

  /** The measures that quantify the Event
    * @return
    *   the list of Event measures
    */
  def measures: Iterable[M]

  /** Returns the top attribute in the hierarchies of this event
    * @return
    *   the hierarchies top attribute
    */
  def topAttribute: A = dimensions.head.hierarchy.last
