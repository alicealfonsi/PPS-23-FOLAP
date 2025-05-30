package folap.core

trait Operational[E]:
  extension (e: E) def sum(other: E)(groupByAttribute: String): E
import MultidimensionalModel._

trait Operational[A <: Attribute, M <: Measure, E <: Event[A, M]]:
  extension (e: E)
    def sum(other: E)(groupBySet: Iterable[String]): E
  extension (events: Iterable[E])
    def aggregateBySum(groupByAttribute: String): E =
      events.tail.foldLeft(events.head)((acc, el) =>
        acc.sum(el)(groupByAttribute)
      )
