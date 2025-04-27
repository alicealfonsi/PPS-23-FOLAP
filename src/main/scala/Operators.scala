import MultidimensionalModel._

object Operators:
  def rollUp[A <: EventAttribute, M <: EventMeasure[_]](
      events: Iterable[Event[A, M]]
  )(
      groupBy: String
  )(createEvent: EventConstructor[A, M]): Iterable[Event[A, M]] =
    if events.matchAttribute(groupBy)
    then
      val groupByMap: Map[Iterable[String], Iterable[Event[A, M]]] =
        events.groupBy(_.attributes.filter(_.name == groupBy).map(_.value))
      var newDimensions: Iterable[A] = List()
      groupByMap.values.foreach(v =>
        newDimensions =
          newDimensions ++ v.head.attributes.filter(_.name == groupBy)
      )
      var aggregatedEvents: Iterable[Event[A, M]] = List()
      for d <- newDimensions do
        val aggregatedEvent: Iterable[Event[A, M]] = List(
          createEvent(List(d), List())
        )
        aggregatedEvents = aggregatedEvents ++ aggregatedEvent
      aggregatedEvents
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
