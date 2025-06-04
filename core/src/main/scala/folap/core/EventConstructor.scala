package folap.core

import multidimensionalmodel.{Attribute, Measure}

/** A type alias for a function to construct an Event given its dimensions and
  * measures
  */
type EventConstructor[A <: Attribute, M <: Measure, E <: Event[A, M]] =
  (Iterable[A], Iterable[M]) => E
