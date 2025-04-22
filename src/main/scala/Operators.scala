import MultidimensionalModel._

object Operators {
  trait Operators:
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

  object Operator extends Operators
}
