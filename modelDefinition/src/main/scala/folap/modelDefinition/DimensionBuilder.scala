package folap.modelDefinition

import folap.modelDefinition.SeqBuilder.ToSeq

case class Dimension(name: String, attributes: Seq[String])

object DimensionBuilder:
  extension (name: String)
    def dimension(attributes: ToSeq[String]): Dimension =
      Dimension(name, attributes.toSeq)
