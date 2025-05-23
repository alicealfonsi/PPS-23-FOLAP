package folap.core.olapDSL

import folap.core.EventAttribute

/** A concrete implementation of EventAttribute used to define attributes in the
  * OLAP DSL.
  *
  * The parent is always set to None, as hierarchical relationships between
  * attributes are not relevant in this context.
  *
  * @param name
  *   name of the attribute (automatically suffixed with "Attribute")
  * @param value
  *   value associated with the attribute
  */
case class AttributeDSL private (
    override val name: String,
    override val value: String
) extends EventAttribute:
  override val parent: Option[EventAttribute] = None

/** Companion object providing factory methods to create AttributeDSL instances.
  *
  * Adds the suffix "Attribute" to the provided base name automatically.
  */
object AttributeDSL:
  /** Creates an AttributeDSL with a specified name and value.
    *
    * @param baseName
    *   base name of the attribute; "Attribute" is appended automatically
    * @param value
    *   value of the attribute
    * @return
    *   attributeDSL instance.
    */
  def apply(baseName: String, value: String): AttributeDSL =
    new AttributeDSL(baseName + "Attribute", value)

  /** Creates an AttributeDSL with a specified name and an empty string as
    * value.
    *
    * @param baseName
    *   base name of the attribute; "Attribute" is appended automatically
    * @return
    *   an AttributeDSL instance with an empty value.
    */
  def apply(baseName: String): AttributeDSL =
    new AttributeDSL(baseName + "Attribute", "")
