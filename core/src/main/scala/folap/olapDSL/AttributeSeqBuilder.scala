package folap.olapDSL
import folap.olapDSL.AttributeDSL.AttributeDSL

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
  extension [L](name: L)
    infix def and(other: L): Seq[(L, String)] =
      Seq((name, ""), (other, ""))

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
  extension [L](seq: Seq[(L, String)])
    infix def and(next: L): Seq[(L, String)] =
      seq :+ (next, "")

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
  extension [L](seq: Seq[(L, String)])
    infix def and(attr: (L, String)): Seq[(L, String)] =
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
  extension [L](attribute: (L, String))
    infix def and(attr: (L, String)): Seq[(L, String)] =
      Seq(attribute, attr)
