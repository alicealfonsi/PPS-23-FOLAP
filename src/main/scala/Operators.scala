/** Operators for querying and manipulating events of a multidimensional data
  * warehouse
  */
object Operators:

  /** "Drill across" operation used to combine events from different cubes that
    * share common attribute names and values.
    *
    * @param events
    *   first set of events
    * @param otherEvents
    *   second set of events to combine with
    * @param createEvent
    *   function to construct a new event from shared attributes and combined
    *   measures
    * @tparam A1
    *   the type of attributes in the first set of events
    * @tparam M1
    *   the type of measures in the first set of events
    * @tparam A2
    *   the type of attributes in the second set of events
    * @tparam M2
    *   the type of measures in the second set of events
    * @return
    *   set of events resulting from the combination of matching events from
    *   both sets
    */
  def drillAcross[
      A1 <: EventAttribute,
      M1 <: EventMeasure[_],
      A2 <: EventAttribute,
      M2 <: EventMeasure[_]
  ](
      events: Iterable[Event[A1, M1]],
      otherEvents: Iterable[Event[A2, M2]],
      createEvent: EventConstructor[A1, M1 | M2]
  ): Iterable[Event[A1, M1 | M2]] = {
    events.flatMap { eventA =>
      otherEvents.flatMap { eventB =>
        val commonAttributes = eventA.attributes.filter { attrA =>
          eventB.attributes.exists(attrB =>
            attrB.name == attrA.name && attrB.value == attrA.value
          )
        }

        if (commonAttributes.nonEmpty) {
          val combinedMeasures: Iterable[M1 | M2] =
            eventA.measures ++ eventB.measures
          Some(createEvent(commonAttributes, combinedMeasures))
        } else {
          None
        }
      }
    }
  }

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
      filters: Iterable[EventAttribute]
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
    /** Finds the attribute of this event whose name is equal to the specified
      * name
      * @param name
      *   the name of the attribute to be found
      * @return
      *   the attribute with the specified name as an iterable
      */
    private def findAttributeByName(name: String): Iterable[A] =
      event.attributes.filter(_.name == name)
  extension [A <: EventAttribute, M <: EventMeasure[_]](
      events: Iterable[Event[A, M]]
  )
    private def matchAttribute(groupBy: String): Boolean =
      events.forall(e =>
        e.attributes.exists(
          _.name == groupBy
        )
      )
