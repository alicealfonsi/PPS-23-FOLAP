import MultidimensionalModel.Attribute

import scala.annotation.tailrec

/** An EventAttribute is an Attribute specific to events related to the same
  * fact
  */
trait EventAttribute extends Attribute:
  /** The attribute that precedes this attribute in the hierarchy
    * @return
    *   the parent attribute
    */
  def parent: Option[EventAttribute]

  /** The hierarchy rooted in this attribute
    * @return
    *   the list of attributes in the hierarchy in ascending order of
    *   aggregation
    */
  def hierarchy: Iterable[EventAttribute] =
    @tailrec
    def recursiveHierarchy(
        attribute: EventAttribute,
        acc: Iterable[EventAttribute]
    ): Iterable[EventAttribute] = attribute.parent match
      case None => acc
      case _ =>
        recursiveHierarchy(
          attribute.parent.get,
          acc ++ List(attribute.parent.get)
        )
    recursiveHierarchy(this, List(this))

  /** Indicates whether this attribute is "equal to" some another attribute
    * @param another
    *   the attribute with which to compare
    * @return
    *   true if this attribute has the same name and value as the other; false
    *   otherwise
    */
  def equals(another: Attribute): Boolean =
    name == another.name && value == another.value

/** The top attribute in the hierarchy
  */
case class TopAttribute() extends EventAttribute:
  override val parent: Option[EventAttribute] = None
  override val value: String = ""
