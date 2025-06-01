package folap.olapDSL

import folap.core.MultidimensionalModel._
import folap.core.Operators.drillAcross
import folap.core.Operators.sliceAndDice
import folap.core._

/** Wraps a QueryDSL (cube) and a roll-up operation (sum, max, min, avg).
  *
  * @param query
  *   queryDSL containing the cube to be rolled up
  * @param op
  *   roll-up operation to apply
  */
case class QueryWithOp[L, A <: Attribute[L], M <: Measure](
    query: QueryDSL[L, A, M],
    op: AggregationOp
)

/** DSL builder object for OLAP queries. Provides natural syntax for olap
  * operations.
  */
object QueryDSLBuilder:

  /** Extension methods for AggregationOp.
    */
  extension [L, A <: Attribute[L], M <: Measure](op: AggregationOp)
    /** Wraps a cube and a roll-up operation into a QueryWithOp.
      *
      * @param q
      *   the query (cube) to which the operation will be applied
      * @return
      *   a QueryWithOp with the cube and operation
      */
    infix def of(q: QueryDSL[L, A, M]): QueryWithOp[L, A, M] =
      QueryWithOp(q, op)

  /** Extension methods for QueryDSL to support slice and dice and drill across
    * operations.
    */
  extension [L, A <: Attribute[L], M <: Measure](q: QueryDSL[L, A, M])
    /** Performs the slice and dice
      *
      * @param filters
      *   attributes used as filters
      * @return
      *   a new QueryDSL instance (cube) with filtered events
      */
    infix def where(filters: Iterable[(L, String)]): QueryDSL[L, A, M] =
      val sliced = sliceAndDice(q.cube, filters)
      QueryDSL(sliced)

    /** Performs the slice and dice
      *
      * @param filter
      *   attribute used as filter
      * @return
      *   a new QueryDSL instance (cube) with filtered events
      */
    infix def where(filter: (L, String)): QueryDSL[L, A, M] =
      val sliced = sliceAndDice(q.cube, Iterable(filter))
      QueryDSL(sliced)

    /** Performs a drill-across between two queries (cubes).
      *
      * @param other
      *   other cube to combine witt
      * @param constructor
      *   given constructor used to create events
      * @return
      *   a new QueryDSL representing the drill-across result
      */
    infix def union[A2 <: Attribute[L], M2 <: Measure](
        other: QueryDSL[L, A2, M2]
    )(using
        constructor: EventConstructor[L, Attribute[L], M | M2]
    ): QueryDSL[L, A | A2, M | M2] =
      val drilled = drillAcross(q.cube, other.cube, constructor)
      ??? // QueryDSL(drilled)
