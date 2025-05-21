package folap.core.olapDSL

import folap.core.Operators.drillAcross
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

    infix def union[A2 <: EventAttribute, M2 <: EventMeasure[_]](
        other: QueryDSL[A2, M2]
    )(using constructor: EventConstructor[A, M | M2]): QueryDSL[A, M | M2] =
      val drilled = drillAcross(q.cube, other.cube, constructor)
      QueryDSL(drilled)
