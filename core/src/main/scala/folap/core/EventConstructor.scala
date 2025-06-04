package folap.core

import multidimensionalModel._

/** A type alias for a function to construct an Event given its dimensions and
  * measures
  */
type EventConstructor[A <: Attribute, M <: Measure] =
  (Iterable[A], Iterable[M]) => Event[A, M]
