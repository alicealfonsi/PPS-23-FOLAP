package folap.core

import MultidimensionalModel._

/** A Cube stores events related to the same fact
  * @tparam A
  *   the type of events attributes
  * @tparam M
  *   the type of events measures
  * @tparam E
  *   the type of events
  */
trait Cube[A <: Attribute, M <: Measure, E <: Event[A, M]]:
  /** The events stored in the Cube
    * @return
    *   the list of Cube events
    */
  def events: Iterable[Event[A, M]]
