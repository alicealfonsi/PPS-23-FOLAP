package folap.typingDSL

import folap.typingDSL.DSLUtils.indent
import folap.typingDSL.DSLUtils.sanitise

object Codegen:
  def generate(dimension: Dimension): String =
    val dimensionName = s"${{ sanitise(dimension.name) }}Dimension"
    val traitAndObjectHead =
      s"sealed trait ${dimensionName}\nobject ${dimensionName}:"
    val objectBody = dimension.attributes
      .zip(dimension.attributes.tail)
      .map((current, parent) => (sanitise(current), sanitise(parent)))
      .map((current, parent) =>
        s"case class ${current}(value: String, parent: ${parent}) extends ${dimensionName}"
      )
      .map(indent(_, 2))
      .mkString("\n")
    val lastLevelName = sanitise(dimension.attributes.last)
    val lastLevel = indent(
      s"case class ${lastLevelName}(value: String) extends ${dimensionName}",
      2
    )

    s"${traitAndObjectHead}\n${objectBody}\n${lastLevel}"

  def generate(t: MeasureType): String =
    "Int"
