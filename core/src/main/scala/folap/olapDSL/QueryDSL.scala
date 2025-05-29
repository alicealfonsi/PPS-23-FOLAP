package folap.olapDSL
import folap.core.MultidimensionalModel._
import folap.core._

/** Represents a collection of OLAP events (cube)
  *
  * @tparam A
  *   type of attribute used in the events, which must extend EventAttribute.
  * @tparam M
  *   type of measure used in the events, which must extend EventMeasure[_].
  * @param cube
  *   a collection of events
  *
  * This class serves as the entry point for DSL-based operations
  */
case class QueryDSL[A <: Attribute, M <: Measure](
    cube: Iterable[Event[A, M]]
)
