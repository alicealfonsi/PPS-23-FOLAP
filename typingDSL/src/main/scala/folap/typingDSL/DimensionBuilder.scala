package folap.typingDSL

import folap.typingDSL.SeqBuilder.ToSeq

case class Dimension(name: String, attributes: Seq[String])

object DimensionBuilder:
  extension (name: String)
    def dimension(attributes: ToSeq[String]): Dimension =
      Dimension(name, attributes.toSeq)
