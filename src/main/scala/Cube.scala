object Cube:
  type Number = Int | Long | Float | Double
  trait Attribute:
    val parent: Option[Attribute]
    val value: String

  trait Measure[T <: Number]:
    val value: T

  trait Event[A <: Attribute]:
    val attributes: Iterable[(String, A)]

  trait Cube
