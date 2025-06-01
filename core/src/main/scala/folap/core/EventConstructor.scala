package folap.core

import MultidimensionalModel._

/** A type alias for a function to construct an Event given its dimensions and
  * measures
  */
type EventConstructor[L, A <: Attribute[L], M <: Measure] =
  (Iterable[A], Iterable[M]) => Event[L, A, M]
