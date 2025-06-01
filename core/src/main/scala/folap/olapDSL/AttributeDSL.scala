package folap.olapDSL
import folap.core.MultidimensionalModel._

/** Companion object providing factory methods to create attribute DSL instances
  * for use in the OLAP DSL.
  *
  * All attribute names are automatically suffixed with "Attribute".
  */
object AttributeDSL:
  trait AttributeDSL[L]:
    def name: L
    def value: String

  /** A concrete implementation of EventAttribute used to define attributes with
    * a value.
    *
    * @param name
    *   name of the attribute
    * @param value
    *   value of the attribute
    */
  case class AttributeDSLWithValue[L](
      val name: L,
      val value: String
  ) extends AttributeDSL[L]

  /** A concrete implementation of EventAttribute used to define attributes
    * without a value.
    *
    * The value is always the empty string.
    *
    * @param name
    *   name of the attribute
    */
  case class AttributeDSLWithoutValue[L](
      val name: L,
      val value: String = ""
  ) extends AttributeDSL[L]

  /** Creates an AttributeDSLWithValue with the specified base name and value.
    *
    * @param baseName
    *   base name of the attribute; "Attribute" is appended automatically
    * @param value
    *   value of the attribute
    * @return
    *   an instance of AttributeDSLWithValue
    */
  def apply[L](baseName: L, value: String): AttributeDSLWithValue[L] =
    AttributeDSLWithValue(baseName, value)

  /** Creates an AttributeDSLWithoutValue with the specified base name.
    *
    * @param baseName
    *   base name of the attribute; "Attribute" is appended automatically
    * @return
    *   an instance of AttributeDSLWithoutValue with empty value
    */
  def apply[L](baseName: L): AttributeDSLWithoutValue[L] =
    AttributeDSLWithoutValue(baseName)
