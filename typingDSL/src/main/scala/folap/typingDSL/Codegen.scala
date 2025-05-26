package folap.typingDSL

import folap.typingDSL.DSLUtils.indent
import folap.typingDSL.DSLUtils.sanitise

object Codegen:
  def generate(dimension: Dimension): String =
    val sanitised = sanitise(dimension.name)
    val traitAndObjectHead =
      s"sealed trait ${sanitised}Dimension\nobject ${sanitised}Dimension:"
    val objectBody = dimension.attributes
      .zip(dimension.attributes.tail)
      .map((current, parent) => (sanitise(current), sanitise(parent)))
      .map((current, parent) =>
        s"case class ${current}(value: String, parent: ${parent})"
      )
      .map(indent(_, 2))
      .mkString("\n")
    val lastLevelName = sanitise(dimension.attributes.last)
    val lastLevel = indent(s"case class ${lastLevelName}(value: String)", 2)

    traitAndObjectHead + objectBody + "\n" + lastLevel
