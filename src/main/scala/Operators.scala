import MultidimensionalModel._

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
    * @tparam A
    *   type of attributes
    * @tparam M
    *   type of measures
    * @return
    *   events resulting from the combination of matching events from both sets
    */
  def drillAcross[A <: Attribute, M <: Measure[_]](
      events: Iterable[Event[A, M]],
      otherEvents: Iterable[Event[A, M]],
      createEvent: EventConstructor[A, M]
  ): Iterable[Event[A, M]] = {
    events.flatMap { eventA =>
      otherEvents.flatMap { eventB =>
        val commonAttributes = eventA.attributes.filter { attrA =>
          eventB.attributes.exists(attrB =>
            attrB.name == attrA.name && attrB.value == attrA.value
          )
        }

        if (commonAttributes.nonEmpty) {
          val combinedMeasures = eventA.measures ++ eventB.measures
          Some(createEvent(commonAttributes, combinedMeasures))
        } else {
          None
        }
      }
    }
  }
