/** Operators for querying and manipulating events of a multidimensional data
  * warehouse
  */
object Operators:

  /** "Slice and dice" operation used to filter events by matching attribute
    * names and values.
    *
    * @param events
    *   events to filter
    * @param filters
    *   attributes to match by name and value
    * @tparam A
    *   type of attributes
    * @tparam M
    *   type of measures
    * @return
    *   events that match all filters
    */

  def sliceAndDice[A <: EventAttribute, M <: EventMeasure[_]](
      events: Iterable[Event[A, M]],
      filters: Iterable[A]
  ): Iterable[Event[A, M]] =
    events.filter { event =>
      filters.forall { filter =>
        event.attributes
          .find(attr => attr.name == filter.name)
          .exists(_.value == filter.value)
      }

    }

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
