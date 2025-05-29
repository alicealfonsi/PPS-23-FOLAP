package folap.modelDefinition

import folap.modelDefinition.DSLUtils.indent
import folap.modelDefinition.DSLUtils.sanitise
import folap.modelDefinition.DSLUtils.toCamelCase

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
      Seq(
        s"case class ${lastLevelName}(value: String) extends ${dimensionName}:",
        "  def parent = Some(Top())",
        s"case class Top() extends ${dimensionName}:",
        "  def parent = Some(folap.core.MultidimensionalModel.TopAttribute())"
      ).mkString("\n"),
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
    Seq(
      s"case class ${name}(value: ${t}) extends folap.core.MultidimensionalModel.Measure:",
      indent(s"type T = ${t}", 2),
      indent(
        s"override def fromRaw(value: ${t}): folap.core.MultidimensionalModel.Measure =",
        2
      ),
      indent(s"${name}(value)", 4)
    ).mkString("\n")

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
      .map(x => (toCamelCase(x), x))
      .map((name, t) => s"${name}: ${t}")
      .mkString(", ")

    val dimensionFields = e.dimensions
      .map(x => sanitise(x.name))
      .map(x => (toCamelCase(x), x))
      .map((name, t) => s"${name}: Dimension.${t}Dimension")
      .mkString(", ")

    val fields = Seq(measureFields, dimensionFields).mkString(", ")

    val allDimensions =
      e.dimensions
        .map(x => sanitise(x.name))
        .map(x => toCamelCase(x))
        .mkString(", ")

    val allMeasureTypes =
      e.measures
        .map(x => sanitise(x.name))

    val allMeasures = allMeasureTypes
      .map(x => toCamelCase(x))
      .mkString(", ")

    val allDimensionAttributes = e.dimensions
      .flatMap((d) => d.attributes.map((d, _)))
      .map((d, a) => (sanitise(d.name), sanitise(a)))
      .map((d, a) => s"${d}Dimension.${a}")
      .mkString(" | ")

    val event =
      indent(
        Seq(
          s"case class ${name}(${fields}) extends folap.core.Event[Dimension, Measures]:",
          s"  def dimensions: Iterable[Dimension] = Seq(${allDimensions})",
          s"  def measures: Iterable[Measures] = Seq(${allMeasures})"
        ).mkString("\n"),
        2
      )
    Seq(
      s"object ${name}:",
      s"${measures}",
      indent("type Measures = " + allMeasureTypes.mkString(" | "), 2),
      "  sealed trait Dimension extends folap.core.MultidimensionalModel.Attribute",
      "  object Dimension:",
      s"${dimensions}",
      indent(s"type Attributes = ${allDimensionAttributes}", 4),
      s"${event}"
    ).mkString("\n")
