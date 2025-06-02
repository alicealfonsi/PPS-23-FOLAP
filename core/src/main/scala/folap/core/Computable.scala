package folap.core

import MultidimensionalModel._

/** The Computable type class
  * @tparam A
  *   the type of Event attributes, which must be a subtype of Attribute
  * @tparam M
  *   the type of Event measures, which must be a subtype of Measure
  * @tparam E
  *   the Computable type, which must be a subtype of Event[A, M]
  */
trait Computable[A <: Attribute, M <: Measure, E <: Event[A, M]]:
  /** Extension methods for instances of E
    */
  extension (e: E)
    /** Performs the sum operation between the Event and the other Event
      * according to the group-by set
      * @param other
      *   the second term of the sum operation
      * @param groupBySet
      *   the names of the attributes against which to perform the sum operation
      * @return
      *   a new Event resulting from the sum operation
      */
    def sum(other: E)(groupBySet: Iterable[String]): E

    /** Performs the operation of dividing the Event by an integer
      * @param n
      *   the integer to divide by
      * @return
      *   a new Event resulting from the division
      */
    def div(n: Int): E

    /** Performs the minimum operation between the Event and the other Event
      * according to the group-by set
      * @param other
      *   the second term of the minimum operation
      * @param groupBySet
      *   the names of the attributes against which to perform the minimum
      *   operation
      * @return
      *   a new Event resulting from the minimum operation
      */
    def min(other: E)(groupBySet: Iterable[String]): E

    /** Performs the maximum operation between the Event and the other Event
      * according to the group-by set
      * @param other
      *   the second term of the maximum operation
      * @param groupBySet
      *   the names of the attributes against which to perform the maximum
      *   operation
      * @return
      *   a new Event resulting from the maximum operation
      */
    def max(other: E)(groupBySet: Iterable[String]): E

    /** Aggregates the Event according to the group-by set
      * @param groupBySet
      *   the names of the attributes against which to aggregate
      * @return
      *   a new Event resulting from the aggregation
      */
    def aggregate(groupBySet: Iterable[String]): E

  import AggregationOp.*

  /** Extension method for instances of Iterable[E]
    */
  extension (events: Iterable[E])
    /** Aggregates the events according to the specified aggregation operator
      * and the group-by set
      * @param op
      *   the aggregation operator
      * @param groupBySet
      *   the names of the attributes against which to aggregate
      * @return
      *   a new Event resulting from the aggregation
      */
    def aggregateBy(op: AggregationOp)(groupBySet: Iterable[String]): E =
      if events.size == 1 then events.head.aggregate(groupBySet)
      else
        op match
          case Sum =>
            events.tail.foldLeft(events.head)((acc, el) =>
              acc.sum(el)(groupBySet)
            )
          case Avg => events.aggregateBy(Sum)(groupBySet) div events.size
          case Min =>
            events.tail.foldLeft(events.head)((acc, el) =>
              acc.min(el)(groupBySet)
            )
          case Max =>
            events.tail.foldLeft(events.head)((acc, el) =>
              acc.max(el)(groupBySet)
            )
