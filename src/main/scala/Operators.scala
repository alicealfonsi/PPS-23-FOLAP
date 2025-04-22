import MultidimensionalModel._
object Operators {
  trait Operators:
    def slice[A <: Attribute, M <: Measure[_]](
        events: Iterable[Event[A, M]],
        filterAttribute: A
    ): Iterable[Event[A, M]] =
      events.filter { event =>
        event.attributes
          .find(attr => attr.name == filterAttribute.name)
          .exists(_.value == filterAttribute.value)
      }

  object Operator extends Operators
}
