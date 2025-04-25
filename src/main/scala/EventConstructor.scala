import MultidimensionalModel.Attribute
import MultidimensionalModel.Event
import MultidimensionalModel.Measure

/** A type alias for a function to construct an Event given its dimensions and
  * measures
  */
type EventConstructor[A <: Attribute, M <: Measure[_]] =
  (Iterable[A], Iterable[M]) => Event[A, M]
