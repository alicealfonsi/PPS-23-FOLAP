object Cube:
  type Number = Int | Long | Float
  trait Attribute:
    val parent: Option[Attribute]
    val value: String

  trait Measure[T <: Number]:
    val value: T