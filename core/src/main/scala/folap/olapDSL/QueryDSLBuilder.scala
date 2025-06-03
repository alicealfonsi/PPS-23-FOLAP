package folap.olapDSL

import folap.core.MultidimensionalModel._
import folap.core.Operators.drillAcross
import folap.core.Operators.rollUp
import folap.core.Operators.sliceAndDice
import folap.core._

/** Wraps a QueryDSL (cube) and a roll-up operation (sum, max, min, avg).
  *
  * @param query
  *   queryDSL containing the cube to be rolled up
  * @param op
  *   roll-up operation to apply
  */
case class QueryWithOp[A <: Attribute, M <: Measure, E <: Event[A, M]](
    query: QueryDSL[A, M, E],
    op: AggregationOp
)

/** DSL builder object for OLAP queries. Provides natural syntax for olap
  * operations.
  */
object QueryDSLBuilder:

  /** Extension methods for AggregationOp to support roll-up operations
    */
  extension [A <: Attribute, M <: Measure, E <: Event[A, M]](op: AggregationOp)
    /** Wraps a cube and a roll-up operation into a QueryWithOp.
      *
      * @param q
      *   the query (cube) to which the operation will be applied
      * @return
      *   a QueryWithOp with the cube and operation
      */
    infix def of(q: QueryDSL[A, M, E]): QueryWithOp[A, M, E] =
      QueryWithOp(q, op)

  /** Extension methods for QueryDSL to support slice and dice and drill across
    * operations.
    */
  extension [A <: Attribute, M <: Measure, E <: Event[A, M]](
      q: QueryDSL[A, M, E]
  )
    /** Performs the slice and dice
      *
      * @param filters
      *   attributes used as filters
      * @return
      *   a new QueryDSL instance (cube) with filtered events
      */
    infix def where(filters: Iterable[Attribute]): QueryDSL[A, M, E] =
      val sliced = sliceAndDice(q.cube, filters)
      QueryDSL(sliced)

    /** Performs the slice and dice
      *
      * @param filter
      *   attribute used as filter
      * @return
      *   a new QueryDSL instance (cube) with filtered events
      */
    infix def where(filter: Attribute): QueryDSL[A, M, E] =
      val sliced = sliceAndDice(q.cube, Iterable(filter))
      QueryDSL(sliced)

    /** Performs a drill across between two queries (cubes).
      *
      * @param other
      *   other cube to combine witt
      * @param constructor
      *   given constructor used to create events
      * @return
      *   a new QueryDSL representing the drill-across result
      */
    infix def union[A2 <: Attribute, M2 <: Measure, E2 <: Event[
      A2,
      M2
    ], E3 <: Event[A, M | M2]](
        other: QueryDSL[A2, M2, E2]
    )(using
        constructor: EventConstructor[A, M | M2, E3]
    ): QueryDSL[A, M | M2, E3] =
      val drilled = drillAcross(q.cube, other.cube, constructor)
      QueryDSL(drilled)

  /** Extension method for QueryWithOp to support roll-up operations.
    */
  extension [A <: Attribute, M <: Measure, M2 <: Measure, E <: Event[A, M]](
      qwo: QueryWithOp[A, M, E]
  )
    /** Aggregates the cube by a set of attributes using the specified roll up
      * operation.
      *
      * @param attributes
      *   attributes to group by
      * @param constructor
      *   implicit constructor used to create events
      * @return
      *   a new QueryDSL containing the rolled-up result.
      */
    def by(attributes: Iterable[Attribute])(using
        operational: Operational[A, M, E]
    ): QueryDSL[A, M, E] =
      QueryDSL(rollUp(qwo.query.cube)(attributes.map(_.name))(qwo.op))

    /** Aggregates the cube by a single attribute using the specified roll up
      * operation.
      *
      * @param attribute
      *   name of the attribute to group by
      * @param constructor
      *   implicit constructor used to create events
      * @return
      *   a new QueryDSL containing the rolled-up result.
      */
    def by(
        attribute: String
    )(using operational: Operational[A, M, E]): QueryDSL[A, M, E] =
      QueryDSL(rollUp(qwo.query.cube)(Iterable(attribute))(qwo.op))
