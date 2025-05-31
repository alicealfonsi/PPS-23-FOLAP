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

object Cube:
  extension [A <: Attribute, M <: Measure](events: Iterable[Event[A, M]])
    /** Tests whether all these events have an Attribute whose name is equal to
      * the specified name
      * @param name
      *   the Attribute name to be matched
      * @return
      *   true if all these events have an Attribute whose name matches the
      *   specified name; false otherwise
      */
    def matchAttributeByName(name: String): Boolean =
      events.forall(_.attributes.exists(_.name == name))
