import MultidimensionalModel.Attribute
import MultidimensionalModel.Event
import MultidimensionalModel.Measure

type EventConstructor[A <: Attribute, M <: Measure[_]] =
  (Iterable[A], Iterable[M]) => Event[A, M]
