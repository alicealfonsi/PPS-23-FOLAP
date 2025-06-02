package folap.core

import MultidimensionalModel._

trait Operational[A <: Attribute, M <: Measure, E <: Event[A, M]]:
  extension (e: E)
    def sum(other: E)(groupBySet: Iterable[String]): E
    def div(n: Int): E
    def min(other: E)(groupBySet: Iterable[String]): E
    def max(other: E)(groupBySet: Iterable[String]): E
    def aggregate(groupBySet: Iterable[String]): E
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
