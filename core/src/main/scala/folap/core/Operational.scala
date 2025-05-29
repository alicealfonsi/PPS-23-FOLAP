package folap.core

trait Operational[E]:
  extension (e: E) def sum(other: E)(groupByAttribute: String): E
