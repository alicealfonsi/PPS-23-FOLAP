package folap.core

import MultidimensionalModel.*

/** A type alias for a function to construct an Event given its dimensions and
  * measures
  */
type EventConstructor[A <: Attribute, M <: Measure[_]] =
  (Iterable[A], Iterable[M]) => Event[A, M]
