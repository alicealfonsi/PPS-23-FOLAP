object Operators:
  def rollUp[A <: EventAttribute, M <: EventMeasure[_]](
      events: Iterable[Event[A, M]]
  )(
      groupBy: String
  )(createEvent: EventConstructor[A, M]): Iterable[Event[A, M]] =
    if events.matchAttribute(groupBy)
    then
      val groupByMap: Map[Iterable[String], Iterable[Event[A, M]]] =
        events.groupBy(_.findAttributeByName(groupBy).map(_.value))
      var newDimensions: Iterable[A] = List()
      groupByMap.values.foreach(v =>
        newDimensions = newDimensions ++ v.head.findAttributeByName(groupBy)
      )
      var aggregatedEvents: Iterable[Event[A, M]] = List()
      for d <- newDimensions do
        val aggregatedEvent: Iterable[Event[A, M]] = List(
          createEvent(List(d), List())
        )
        aggregatedEvents = aggregatedEvents ++ aggregatedEvent
      aggregatedEvents
    else events

  extension [A <: EventAttribute, M <: EventMeasure[_]](event: Event[A, M])
    private def findAttributeByName(attribute: String): Iterable[A] =
      event.attributes.filter(_.name == attribute)

  extension [A <: EventAttribute, M <: EventMeasure[_]](
      events: Iterable[Event[A, M]]
  )
    private def matchAttribute(groupBy: String): Boolean =
      events.forall(e =>
        e.attributes.exists(
          _.name == groupBy
        )
      )
