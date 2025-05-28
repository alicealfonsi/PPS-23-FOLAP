package folap.core

import scala.annotation.tailrec

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

    /** The attribute that precedes this attribute in the hierarchy
      * @return
      *   the parent attribute
      */
    def parent: Option[Attribute]

    /** The hierarchy rooted in this attribute
      * @return
      *   the list of attributes in the hierarchy in ascending order of
      *   aggregation
      */
    def hierarchy: Iterable[Attribute] =
      @tailrec
      def recursiveHierarchy(
          attribute: Attribute,
          acc: Iterable[Attribute]
      ): Iterable[Attribute] = attribute.parent match
        case None => acc
        case Some(p) =>
          recursiveHierarchy(
            p,
            acc ++ List(p)
          )
      recursiveHierarchy(this, List(this))

    /** Indicates whether this attribute is "equal to" the other attribute
      * @param other
      *   the attribute with which to compare
      * @return
      *   true if this attribute has the same name and value as the other; false
      *   otherwise
      */
    def equals(other: Attribute): Boolean =
      name == other.name && value == other.value

  /** The top attribute in the hierarchy
    */
  case class TopAttribute() extends Attribute:
    override val parent: Option[Attribute] = None
    override val value: String = ""

  /** A Measure represents a numeric value associated with an Event
    */
  trait Measure:
    /** Abstract type member representing the type of the Measure
      */
    type T <: MeasureType

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

    /** Create a Measure from the raw value
      * @param value
      *   the raw value
      * @return
      *   a Measure
      */
    def fromRaw(value: T): Measure
