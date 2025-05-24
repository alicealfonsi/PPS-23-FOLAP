package folap.typingDSL

case class Dimension(name: String, attributes: Seq[DimensionAttribute])

object DimensionBuilder:
  extension (name: String)
    def dimension(attributes: Seq[DimensionAttribute]): Dimension =
      Dimension(name, attributes)
