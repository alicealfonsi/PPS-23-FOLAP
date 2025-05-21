package folap.core.olapDSL
import folap.core._
case class QueryDSL[A <: EventAttribute, M <: EventMeasure[_]](
    cube: Iterable[Event[A, M]]
)
