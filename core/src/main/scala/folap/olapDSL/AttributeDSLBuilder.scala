package folap.olapDSL
import folap.olapDSL.AttributeDSL.AttributeDSL

/** Provides a simple and readable DSL for creating AttributeDSL instances.
  */
object AttributeDSLBuilder:
  /** Extension method that allows creating an AttributeDSL instance using
    * natural syntax with the infix `is` keyword.
    *
    * @param name
    *   name of the attribute.
    * @param value
    *   value of the attribute.
    * @return
    *   an AttributeDSL with the specified name and value.
    */
  extension (name: String)
    infix def is(value: String): AttributeDSL =
      AttributeDSL(name, value)
