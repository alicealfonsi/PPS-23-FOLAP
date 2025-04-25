import MultidimensionalModel._

object Operators:
  def rollUp[A <: EventAttribute, M <: EventMeasure[_]](
      events: Iterable[Event[A, M]]
  )(groupBy: String): Iterable[Event[A, M]] =
    if events.forall(e =>
        e.dimensions.exists(
          _.name == groupBy
        )
      )
    then List()
    else events
