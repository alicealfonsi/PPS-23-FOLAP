import MultidimensionalModel._

object Operators:
  def rollUp[A <: EventAttribute, M <: EventMeasure[_]](
      events: Iterable[Event[A, M]]
  )(groupBy: String): Iterable[Event[A, M]] =
    if events.matchAttribute(groupBy)
    then List()
    else events

  extension [A <: EventAttribute, M <: EventMeasure[_]](
      events: Iterable[Event[A, M]]
  )
    private def matchAttribute(groupBy: String): Boolean =
      events.forall(e =>
        e.attributes.exists(
          _.name == groupBy
        )
      )
