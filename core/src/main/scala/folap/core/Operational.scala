package folap.core

import MultidimensionalModel._

trait Operational[A <: Attribute, M <: Measure, E <: Event[A, M]]:
  extension (e: E)
    def sum(other: E)(groupBySet: Iterable[String]): E
    def div(n: Int): E
    def aggregate(groupBySet: Iterable[String]): E
  extension (events: Iterable[E])
    def aggregateBySum(groupBySet: Iterable[String]): E =
      if events.size == 1 then events.head.aggregate(groupBySet)
      else
        events.tail.foldLeft(events.head)((acc, el) => acc.sum(el)(groupBySet))
    def aggregateByAverage(groupBySet: Iterable[String]): E =
      events.aggregateBySum(groupBySet) div events.size
