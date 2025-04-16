/** The Cube defines the types needed to implement a multidimensional cube
  */
object Cube:

  /** An Attribute occupies a single level in a hierarchy
    */
  trait Attribute:
    val parent: Option[Attribute]
    val value: String

  /** A Measure represent a numeric value associated with an Event
    * @tparam T
    *   the underlying measure type
    */
  trait Measure[T](implicit num: Numeric[T])
      extends Equiv[Measure[T]]
      with Comparable[Measure[T]]:

    import scala.math.Numeric.Implicits.infixNumericOps

    /** The underlying measure value
      */
    val value: T

    override def equiv(x: Measure[T], y: Measure[T]): Boolean =
      x.value == y.value

    override def compareTo(o: Measure[T]): Int =
      import scala.math.Ordered.orderingToOrdered
      value.compare(o.value)

    /**
     * Create a Measure from the raw value
     * @param value the raw value
     * @return a Measure
     */
    def fromRaw(value: T): Measure[T]

    def +(other: Measure[T]): Measure[T] =
      fromRaw(value + other.value)

    def -(other: Measure[T]): Measure[T] =
      fromRaw(value - other.value)

  /** An Event is something that happened in the business domain
    * @tparam A
    *   The attributes union type
    */
  trait Event[A <: Attribute]:
    /** List all attributes. It must return all attributes contained in the
      * event.
      */
    val attributes: Iterable[(String, A)]

  trait Cube
