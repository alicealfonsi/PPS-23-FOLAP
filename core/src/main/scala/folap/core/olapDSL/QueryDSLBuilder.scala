package folap.core.olapDSL

import folap.core.Operators.sliceAndDice
import folap.core._

object QueryDSLBuilder:

  extension [A <: EventAttribute, M <: EventMeasure[_]](q: QueryDSL[A, M])
    infix def where(filters: Iterable[EventAttribute]): QueryDSL[A, M] =
      val sliced = sliceAndDice(q.cube, filters)
      QueryDSL(sliced)

    infix def where(filter: EventAttribute): QueryDSL[A, M] =
      val sliced = sliceAndDice(q.cube, Seq(filter))
      QueryDSL(sliced)
