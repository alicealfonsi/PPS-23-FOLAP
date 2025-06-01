package folap.core

import MultidimensionalModel._

trait Operational[L, A <: Attribute[L], M <: Measure, E <: Event[L, A, M]]:
  extension (e: E)
    def sum(other: E)(groupBySet: Iterable[L]): E
    def div(n: Int): E
    def min(other: E)(groupBySet: Iterable[L]): E
    def max(other: E)(groupBySet: Iterable[L]): E
    def aggregate(groupBySet: Iterable[L]): E
  extension (events: Iterable[E])
    def aggregateBySum(groupBySet: Iterable[L]): E =
      if events.size == 1 then events.head.aggregate(groupBySet)
      else
        events.tail.foldLeft(events.head)((acc, el) => acc.sum(el)(groupBySet))
    def aggregateByAverage(groupBySet: Iterable[L]): E =
      events.aggregateBySum(groupBySet) div events.size
    def aggregateByMinimum(groupBySet: Iterable[L]): E =
      if events.size == 1 then events.head.aggregate(groupBySet)
      else
        events.tail.foldLeft(events.head)((acc, el) => acc.min(el)(groupBySet))
    def aggregateByMaximum(groupBySet: Iterable[L]): E =
      if events.size == 1 then events.head.aggregate(groupBySet)
      else
        events.tail.foldLeft(events.head)((acc, el) => acc.max(el)(groupBySet))
