/** The model for representing and querying data in a DW
  */
object MultidimensionalModel:

  /** An Attribute occupies a single level in a hierarchy
    */
  trait Attribute:
    /** The attribute that precedes this attribute in the hierarchy
      * @return
      *   the parent attribute
      */
    def parent: Option[Attribute]

    /** The name of the attribute
      * @return
      *   the attribute name
      */
    def name: String = getClass.getName

    /** The value of the attribute
      * @return
      *   the attribute value
      */
    def value: String

    /** Indicates whether this attribute is "equal to" some other attribute
      * @param other
      *   the attribute with which to compare
      * @return
      *   true if this attribute has the same name and value as the other; false
      *   otherwise
      */
    def equals(other: Attribute): Boolean =
      name == other.name && value == other.value

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
    def name: String = getClass.getName

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

  /** An Event is an instance of a fact that occurred in the business domain
    * @tparam A
    *   the attributes union type
    * @tparam M
    *   the measures union type
    */
  trait Event[A <: Attribute, M <: Measure[_]]:
    /** The attributes that describe the Event
      * @return
      *   the list of Event attributes
      */
    def attributes: Iterable[A]

    /** The measures that quantify the Event
      * @return
      *   the list of Event measures
      */
    def measures: Iterable[M]

  /** A Cube stores events related to the same fact
    * @tparam A
    *   the attributes union type
    * @tparam M
    *   the measures union type
    * @tparam E
    *   the type of events
    */
  trait Cube[A <: Attribute, M <: Measure[_], E <: Event[A, M]]:
    /** The events stored in the Cube
      * @return
      *   the list of Cube events
      */
    def events: Iterable[Event[A, M]]
