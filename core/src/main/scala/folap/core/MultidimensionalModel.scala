package folap.core

/** The model for representing and querying data in a DW
  */
object MultidimensionalModel:

  /** An Attribute is a finite domain property of an Event
    */
  trait Attribute:
    /** The name of the attribute
      * @return
      *   the attribute name
      */
    def name: String = getClass.getSimpleName

    /** The value of the attribute
      * @return
      *   the attribute value
      */
    def value: String

  /** A Measure represents a numeric value associated with an Event
    * @tparam T
    *   the measure type
    */
  trait Measure[T](implicit num: Numeric[T])
      extends Equiv[Measure[T]]
      with Comparable[Measure[T]]:

    import scala.math.Numeric.Implicits.infixNumericOps

    /** The name of the measure
      * @return
      *   the measure name
      */
    def name: String = getClass.getSimpleName

    /** The value of the measure
      * @return
      *   the measure value
      */
    def value: T

    override def equiv(x: Measure[T], y: Measure[T]): Boolean =
      x.value == y.value

    override def compareTo(o: Measure[T]): Int =
      import scala.math.Ordered.orderingToOrdered
      value.compare(o.value)

    /** Create a Measure from the raw value
      * @param value
      *   the raw value
      * @return
      *   a Measure
      */
    def fromRaw(value: T): Measure[T]

    def +(other: Measure[T]): Measure[T] =
      fromRaw(value + other.value)

    def -(other: Measure[T]): Measure[T] =
      fromRaw(value - other.value)
