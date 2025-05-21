package folap.typingDSL

case class Dimension(name: String, attributes: Seq[String])

object DimensionBuilder:
  extension (name: String)
    def dimension(attributes: Seq[String]): Dimension =
      Dimension(name, attributes)
