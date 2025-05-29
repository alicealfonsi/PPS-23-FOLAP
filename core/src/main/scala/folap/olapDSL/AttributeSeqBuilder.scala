package folap.core.olapDSL
import folap.core.olapDSL.AttributeDSL.AttributeDSL


/** Provides a DSL for building sequences of AttributeDSL objects using
  * intuitive and readable syntax with the infix `and` keyword.
  */
object AttributeSeqBuilder:

  /** Extension method that allows combining two attribute names (as strings)
    * into a sequence of AttributeDSL with empty values.
    *
    * @param name
    *   first attribute name
    * @param other
    *   second attribute name
    * @return
    *   a sequence of AttributeDSL with empty values
    */
  extension (name: String)
    infix def and(other: String): Seq[AttributeDSL] =
      Seq(AttributeDSL(name), AttributeDSL(other))

  /** Extension method that allows appending a new attribute (by name, with
    * empty value) to an existing sequence.
    *
    * @param seq
    *   existing sequence of attributes
    * @param next
    *   name of the new attribute to append
    * @return
    *   updated sequence including the new attribute
    */
  extension (seq: Seq[AttributeDSL])
    infix def and(next: String): Seq[AttributeDSL] =
      seq :+ AttributeDSL(next)

  /** Extension method that allows appending an AttributeDSL to an existing
    * sequence of AttributeDSL.
    *
    * @param seq
    *   existing sequence of attributes
    * @param attr
    *   attribute to append
    * @return
    *   updated sequence including the new attribute
    */
  extension (seq: Seq[AttributeDSL])
    infix def and(attr: AttributeDSL): Seq[AttributeDSL] =
      seq :+ attr

  /** Extension method that allows combining two attributes into a sequence of
    * AttributeDSL.
    *
    * @param name
    *   first attribute
    * @param other
    *   second attribute
    * @return
    *   a sequence of AttributeDSL
    */
  extension (attribute: AttributeDSL)
    infix def and(attr: AttributeDSL): Seq[AttributeDSL] =
      Seq(attribute, attr)
