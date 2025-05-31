package folap.core

import MultidimensionalModel._

/** A type alias for a function to construct an Event given its dimensions and
  * measures
  */
type EventConstructor[A <: Attribute, M <: Measure, E <: Event[A, M]] =
  (Iterable[A], Iterable[M]) => E
