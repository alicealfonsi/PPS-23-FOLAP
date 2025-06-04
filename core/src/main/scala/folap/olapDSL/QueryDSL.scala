package folap.olapDSL
import folap.core._
import folap.core.multidimensionalmodel.{Attribute, Measure}

/** Represents a collection of OLAP events (cube)
  *
  * @tparam A
  *   type of attribute used in the events, which must extend Attribute.
  * @tparam M
  *   type of measure used in the events, which must extend Measure.
  * @param cube
  *   a collection of events
  *
  * This class serves as the entry point for DSL-based operations
  */
case class QueryDSL[A <: Attribute, M <: Measure, E <: Event[A, M]](
    cube: Iterable[E]
)
