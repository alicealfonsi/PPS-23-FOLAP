package folap.typingDSL

import folap.typingDSL.DSLUtils.indent
import folap.typingDSL.DSLUtils.sanitise
import folap.typingDSL.DSLUtils.toCamelCase

object Codegen:
  def generate(dimension: Dimension): String =
    val dimensionName = s"${{ sanitise(dimension.name) }}Dimension"
    val traitAndObjectHead =
      s"sealed trait ${dimensionName} extends Dimension\nobject ${dimensionName}:"
    val objectBody = dimension.attributes
      .zip(dimension.attributes.tail)
      .map((current, parent) => (sanitise(current), sanitise(parent)))
      .map((current, parent) => (current, parent, toCamelCase(parent)))
      .map((current, parent, parentCamelCase) =>
        Seq(
          s"case class ${current}(value: String, ${parentCamelCase}: ${parent}) extends ${dimensionName}:",
          s"  def parent = Some(${parentCamelCase})"
        ).mkString("\n")
      )
      .map(indent(_, 2))
      .mkString("\n")
    val lastLevelName = sanitise(dimension.attributes.last)
    val lastLevel = indent(
      s"case class ${lastLevelName}(value: String) extends ${dimensionName}:\n  def parent = Some(folap.core.MultidimensionalModel.TopAttribute())",
      2
    )

    s"${traitAndObjectHead}\n${objectBody}\n${lastLevel}"

  def generate(t: MeasureType): String =
    t match
      case Int    => "Int"
      case Long   => "Long"
      case Float  => "Float"
      case Double => "Double"

  def generate(m: Measure): String =
    val name = sanitise(m.name)
    val t = generate(m.typology)
    s"case class ${name}(value: ${t})"

  def generate(e: Event): String =
    val name = sanitise(e.name)
    val measures = e.measures
      .map(generate(_))
      .map(indent(_, 2))
      .mkString("\n")
    val dimensions = e.dimensions
      .map(generate(_))
      .map(indent(_, 4))
      .mkString("\n")

    val measureFields = e.measures
      .map(x => sanitise(x.name))
      .map(x => s"${{ x.toLowerCase() }}: ${x}")
      .mkString(", ")

    val dimensionFields = e.dimensions
      .map(x => sanitise(x.name))
      .map(x => s"${{ x.toLowerCase() }}: Dimension.${x}Dimension")
      .mkString(", ")

    val fields = Seq(measureFields, dimensionFields).mkString(", ")

    val event =
      s"  case class ${name}(${fields})"
    s"object ${name}:\n${measures}\n  sealed trait Dimension\n  object Dimension:\n${dimensions}\n${event}"
