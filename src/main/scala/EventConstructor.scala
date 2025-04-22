import MultidimensionalModel.{Attribute, Measure, Event}

type EventConstructor[A <: Attribute, M <: Measure[_]] =
  (Iterable[A], Iterable[M]) => Event[A, M]
