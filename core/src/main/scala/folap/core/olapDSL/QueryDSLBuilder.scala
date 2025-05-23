package folap.core.olapDSL

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
case class QueryWithOp[A <: EventAttribute, M <: EventMeasure[_]](
    query: QueryDSL[A, M],
    op: RollupOp
)

/** Helper class used to begin a roll up operation in the DSL with natural
  * syntax such as max of, min of, avg of, and sum of."
  *
  * @param op
  *   name of the operation as a string ("sum", "max", "min", "avg")
  */
case class OpWord[A <: EventAttribute, M <: EventMeasure[_]](op: String):
  /** produce a QueryWithOp.
    *
    * @param q
    *   queryDSL (cube) to which the operation should be applied.
    * @return
    *   a QueryWithOp containing the query and the parsed operation.
    */
  def of(q: QueryDSL[A, M]): QueryWithOp[A, M] =
    val rollupOp = op match
      case "max" => RollupOp.Max
      case "min" => RollupOp.Min
      case "avg" => RollupOp.Avg
      case "sum" => RollupOp.Sum
    QueryWithOp(q, rollupOp)

/** DSL builder object for OLAP queries. Provides natural syntax for olap
  * operations
  */
object QueryDSLBuilder:
  /** Initiates a roll up operation with operator "max" on a query (cube). */
  def max[A <: EventAttribute, M <: EventMeasure[_]]: OpWord[A, M] = OpWord(
    "max"
  )

  /** Initiates a roll up operation with operator "min" on a query (cube). */
  def min[A <: EventAttribute, M <: EventMeasure[_]]: OpWord[A, M] = OpWord(
    "min"
  )

  /** Initiates a roll up operation with operator "avg" on a query (cube). */
  def avg[A <: EventAttribute, M <: EventMeasure[_]]: OpWord[A, M] = OpWord(
    "avg"
  )

  /** Initiates a roll up operation with operator "sum" on a query (cube). */
  def sum[A <: EventAttribute, M <: EventMeasure[_]]: OpWord[A, M] = OpWord(
    "sum"
  )

  /** Extension methods for QueryDSL to support slice and dice and drill across
    * operations.
    */
  extension [A <: EventAttribute, M <: EventMeasure[_]](q: QueryDSL[A, M])
    /** Performs the slice and dice
      *
      * @param filters
      *   attributes used as filters
      * @return
      *   a new QueryDSL instance (cube) with filtered events
      */
    infix def where(filters: Iterable[EventAttribute]): QueryDSL[A, M] =
      val sliced = sliceAndDice(q.cube, filters)
      QueryDSL(sliced)

    /** Performs the slice and dice
      *
      * @param filter
      *   attribute used as filter
      * @return
      *   a new QueryDSL instance (cube) with filtered events
      */
    infix def where(filter: EventAttribute): QueryDSL[A, M] =
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
    infix def union[A2 <: EventAttribute, M2 <: EventMeasure[_]](
        other: QueryDSL[A2, M2]
    )(using constructor: EventConstructor[A, M | M2]): QueryDSL[A, M | M2] =
      val drilled = drillAcross(q.cube, other.cube, constructor)
      QueryDSL(drilled)

  /** Extension method for QueryWithOp to support roll-up operations.
    */
  extension [A <: EventAttribute, M <: EventMeasure[_], M2 <: EventMeasure[_]](
      qwo: QueryWithOp[A, M]
  )
    /** Aggregates the cube by a set of attributes using the specified roll-up
      * operation.
      *
      * @param attributes
      *   attributes to group by
      * @param constructor
      *   implicit constructor used to create events
      * @return
      *   a new QueryDSL containing the rolled-up result.
      */
    def by(attributes: Iterable[AttributeDSL])(using
        constructor: EventConstructor[A, M | M2]
    ): QueryDSL[A, M] =
      QueryDSL(rollUp(qwo.query.cube, attributes, qwo.op, constructor))

    /** Aggregates the cube by a single attribute using the specified roll-up
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
    )(using constructor: EventConstructor[A, M | M2]): QueryDSL[A, M] =
      QueryDSL(
        rollUp(
          qwo.query.cube,
          Iterable(AttributeDSL(attribute)),
          qwo.op,
          constructor
        )
      )
