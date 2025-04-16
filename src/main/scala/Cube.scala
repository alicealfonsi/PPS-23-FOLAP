object Cube:
  type Number = Int | Long | Float | Double
  trait Attribute:
    val parent: Option[Attribute]
    val value: String

  trait Measure[T](implicit num: Numeric[T])
      extends Equiv[Measure[T]]
      with Comparable[Measure[T]]:

    val value: T

    override def equiv(x: Measure[T], y: Measure[T]): Boolean =
      x.value == y.value

    override def compareTo(o: Measure[T]): Int =
      import scala.math.Ordered.orderingToOrdered
      value.compare(o.value)

  trait Event[A <: Attribute]:
    val attributes: Iterable[(String, A)]

  trait Cube
