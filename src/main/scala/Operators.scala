import MultidimensionalModel._
object Operators:
  
  def sliceAndDice[A <: Attribute, M <: Measure[_]](      
        events: Iterable[Event[A, M]],
        filters: Iterable[Attribute]
    ): Iterable[Event[A, M]] =
      events.filter { event =>
        filters.forall { filter =>
          event.attributes
            .find(attr => attr.name == filter.name)
            .exists(_.value == filter.value)
        }

      }

  

