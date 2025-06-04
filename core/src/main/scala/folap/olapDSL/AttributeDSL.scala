package folap.olapDSL
import folap.core.multidimensionalmodel.Attribute

/** Object providing factory methods to create attribute DSL instances for use
  * in the OLAP DSL.
  */
object AttributeDSL:
  trait AttributeDSL extends Attribute

  /** A concrete implementation of `Attribute` used to define attributes with a
    * value.
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
    override val parent: Option[Attribute] = None

  /** A concrete implementation of `Attribute` used to define attributes without
    * a value.
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
    override val parent: Option[Attribute] = None

  /** Creates an AttributeDSLWithValue with the specified base name and value.
    *
    * @param baseName
    *   nase name of the attribute
    * @param value
    *   value of the attribute
    * @return
    *   an instance of AttributeDSLWithValue
    */
  def apply(name: String, value: String): AttributeDSLWithValue =
    AttributeDSLWithValue(name, value)

  /** Creates an AttributeDSLWithoutValue with the specified base name.
    *
    * @param baseName
    *   name of the attribute
    * @return
    *   an instance of AttributeDSLWithoutValue with empty value
    */
  def apply(name: String): AttributeDSLWithoutValue =
    AttributeDSLWithoutValue(name)
