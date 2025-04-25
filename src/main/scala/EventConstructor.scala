import MultidimensionalModel._

/** A type alias for a function to construct an Event given its dimensions and
  * measures
  */
type EventConstructor[A <: EventAttribute, M <: EventMeasure[_]] =
  (Iterable[A], Iterable[M]) => Event[A, M]
