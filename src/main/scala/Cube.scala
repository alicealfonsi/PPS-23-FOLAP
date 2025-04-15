object Cube:
  trait Attribute:
    val parent: Option[Attribute]
    val value: String

  trait Measure:
    val value: Int