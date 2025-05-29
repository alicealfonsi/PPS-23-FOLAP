package folap.core.olapDSL

import folap.core.EventAttribute

/** Companion object providing factory methods to create attribute DSL instances
  * for use in the OLAP DSL.
  *
  * All attribute names are automatically suffixed with "Attribute".
  */
object AttributeDSL:
  trait AttributeDSL extends EventAttribute

  /** A concrete implementation of EventAttribute used to define attributes with
    * a value.
    *
    * @param name
    *   name of the attribute
    * @param value
    *   value of the attribute
    */
  case class AttributeDSLWithValue(
      override val name: String,
      override val value: String
  ) extends AttributeDSL:
    override val parent: Option[EventAttribute] = None

  /** A concrete implementation of EventAttribute used to define attributes
    * without a value.
    *
    * The value is always the empty string.
    *
    * @param name
    *   name of the attribute
    */
  case class AttributeDSLWithoutValue(
      override val name: String,
      override val value: String = ""
  ) extends AttributeDSL:
    override val parent: Option[EventAttribute] = None

  /** Creates an AttributeDSLWithValue with the specified base name and value.
    *
    * @param baseName
    *   base name of the attribute; "Attribute" is appended automatically
    * @param value
    *   value of the attribute
    * @return
    *   an instance of AttributeDSLWithValue
    */
  def apply(baseName: String, value: String): AttributeDSLWithValue =
    AttributeDSLWithValue(baseName + "Attribute", value)

  /** Creates an AttributeDSLWithoutValue with the specified base name.
    *
    * @param baseName
    *   base name of the attribute; "Attribute" is appended automatically
    * @return
    *   an instance of AttributeDSLWithoutValue with empty value
    */
  def apply(baseName: String): AttributeDSLWithoutValue =
    AttributeDSLWithoutValue(baseName + "Attribute")
