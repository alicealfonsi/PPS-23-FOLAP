package folap.core

import scala.annotation.tailrec

/** The model for representing and querying data in a DW
  */
object MultidimensionalModel:

  /** An Attribute is a finite domain property of an Event
    */
  trait Attribute:
    /** The name of the Attribute
      * @return
      *   the Attribute name
      */
    def name: String = getClass.getSimpleName

    /** The value of the Attribute
      * @return
      *   the Attribute value
      */
    def value: String

    /** The Attribute that precedes this Attribute in the hierarchy
      * @return
      *   a Some containing the parent Attribute if this Attribute is not the
      *   last in the hierarchy; None otherwise
      */
    def parent: Option[Attribute]

    /** The hierarchy rooted in this Attribute
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

    /** Indicates whether this Attribute is "equal to" the other Attribute
      * @param other
      *   the Attribute with which to compare
      * @return
      *   true if this Attribute has the same name and value as the other; false
      *   otherwise
      */
    def equals(other: Attribute): Boolean =
      name == other.name && value == other.value

  /** The top Attribute in the hierarchy
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

    /** The name of the Measure
      * @return
      *   the Measure name
      */
    def name: String = getClass.getSimpleName

    /** The value of the Measure
      * @return
      *   the Measure value
      */
    def value: T

    /** Creates a Measure from the raw value
      * @param value
      *   the raw value
      * @return
      *   the Measure containing the value
      */
    def fromRaw(value: T): Measure
