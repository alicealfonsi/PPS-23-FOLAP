package folap.core.olapDSL

import folap.core.Operators.drillAcross
import folap.core.Operators.rollUp
import folap.core.Operators.sliceAndDice
import folap.core._

case class QueryWithOp[A <: EventAttribute, M <: EventMeasure[_]](
    query: QueryDSL[A, M],
    op: RollupOp
)

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

    def max: QueryWithOp[A, M] = QueryWithOp(q, RollupOp.Max)
    def min: QueryWithOp[A, M] = QueryWithOp(q, RollupOp.Min)
    def avg: QueryWithOp[A, M] = QueryWithOp(q, RollupOp.Avg)
  extension [A <: EventAttribute, M <: EventMeasure[_], M2 <: EventMeasure[_]](
      qwo: QueryWithOp[A, M]
  )
    def by(attributes: Iterable[AttributeDSL])(using
        constructor: EventConstructor[A, M | M2]
    ): QueryDSL[A, M] =
      QueryDSL(rollUp(qwo.query.cube, attributes, qwo.op, constructor))
  extension [A <: EventAttribute, M <: EventMeasure[_], M2 <: EventMeasure[_]](
      qwo: QueryWithOp[A, M]
  )
    def by(
        attribute: String
    )(using constructor: EventConstructor[A, M | M2]): QueryDSL[A, M] =
      QueryDSL(
        rollUp(
          qwo.query.cube,
          Iterable(AttributeDSL(attribute)),
          qwo.op,
          constructor
        )
      )
