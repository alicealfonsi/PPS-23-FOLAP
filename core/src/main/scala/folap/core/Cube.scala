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
trait Cube[L, A <: Attribute[L], M <: Measure, E <: Event[L, A, M]]:
  /** The events stored in the Cube
    * @return
    *   the list of Cube events
    */
  def events: Iterable[Event[L, A, M]]

object Cube:
  extension [L, A <: Attribute[L], M <: Measure](events: Iterable[Event[L, A, M]])
    /** Tests whether all these events have an Attribute whose name is equal to
      * the specified name
      * @param name
      *   the Attribute name to be matched
      * @return
      *   true if all these events have an Attribute whose name matches the
      *   specified name; false otherwise
      */
    def matchAttributeByName(name: L): Boolean =
      events.forall(_.attributes.exists(_.name == name))
