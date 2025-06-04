package folap.modeldefinition

import folap.modeldefinition.SeqBuilder.ToSeq

case class Dimension(name: String, attributes: Seq[String])

object DimensionBuilder:
  extension (name: String)
    /** Build a dimension with the given name and attributes
      *
      * @param attributes
      *   the dimensional attributes
      * @return
      *   A Dimension for which code can be generated
      */
    def dimension(attributes: ToSeq[String]): Dimension =
      Dimension(name, attributes.toSeq)
