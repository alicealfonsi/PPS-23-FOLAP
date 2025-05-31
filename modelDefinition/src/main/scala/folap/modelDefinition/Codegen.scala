package folap.modelDefinition

import folap.modelDefinition.CodegenUtils.indent
import folap.modelDefinition.CodegenUtils.sanitise
import folap.modelDefinition.CodegenUtils.toCamelCase

object Codegen:
  /** Generate the Scala code for the given Dimension
    *
    * @param dimension
    *   The dimension to generate
    * @return
    *   The generated code
    */
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
        "  def parent = Some(TopAttribute())",
        s"case class TopAttribute() extends ${dimensionName}:",
        "  def parent = None",
        "  def value = \"\""
      ).mkString("\n"),
      2
    )

    s"${traitAndObjectHead}\n${objectBody}\n${lastLevel}"

  /** Map the MeasureType to a Scala type as a String
    *
    * @param t
    *   the Measure's type
    * @return
    *   the type as a string
    */
  def generate(t: MeasureType): String =
    t match
      case Int        => "Int"
      case Long       => "Long"
      case Float      => "Float"
      case Double     => "Double"
      case BigInt     => "BigInt"
      case BigDecimal => "BigDecimal"

  /** Generate the code for a Measure
    *
    * @param m
    *   the Measure to be generated
    * @return
    *   the generated code for the provided Measure
    */
  def generate(m: Measure): String =
    val name = sanitise(m.name)
    val t = generate(m.typology)
    Seq(
      s"case class ${name}(value: ${t}) extends folap.core.MultidimensionalModel.Measure:",
      indent(s"type T = ${t}", 2)
    ).mkString("\n")

  private def generateGiven(e: Event): String =
    val name = sanitise(e.name)
    val measures = e.measures
      .map(_.name)
      .map(sanitise(_))
      .map(x => (toCamelCase(x), x))

    val dimensions = e.dimensions
      .map(_.name)
      .map(sanitise(_))
      .map(toCamelCase(_))

    val sourceMeasures = measures.map((x, _) => s"e.${x}")
    val sourceDimensions = dimensions.map(x => s"e.${x}")
    val mappedDimensions = dimensions
      .map(x =>
        s"e.${x}.upToLevel(e.${x}.searchCorrespondingAttributeName(groupBySet))"
      )

    val summedMeasures = measures
      .map((x, t) => s"${t}(aggregated.${x}.value + other.${x}.value)")
    val divvedMeasures = measures
      .map((x, t) => s"${t}(e.${x}.value / n)")
    val minMeasures = measures
      .map((x, t) => s"${t}(aggregated.${x}.value.min(other.${x}.value))")
    val maxMeasures = measures
      .map((x, t) => s"${t}(aggregated.${x}.value.max(other.${x}.value))")
    val aggregatedDimensions = dimensions.map(x => s"aggregated.${x}")

    Seq(
      s"given folap.core.Operational[Dimension, Measures, ${name}] with",
      s"  extension (e: ${name})",
      s"    override def aggregate(groupBySet: Iterable[String]): ${name} =",
      indent(
        (sourceMeasures ++ mappedDimensions)
          .mkString(s"${name}(", ", ", ")"),
        6
      ),
      s"    override def sum(other: ${name})(groupBySet: Iterable[String]): ${name} =",
      "      val aggregated = e.aggregate(groupBySet)",
      indent(
        (summedMeasures ++ aggregatedDimensions)
          .mkString(s"${name}(", ", ", ")"),
        6
      ),
      s"    override def div(n: Int): ${name} =",
      indent(
        (divvedMeasures ++ sourceDimensions)
          .mkString(s"${name}(", ", ", ")"),
        6
      ),
      s"    override def min(other: ${name})(groupBySet: Iterable[String]): ${name} =",
      "      val aggregated = e.aggregate(groupBySet)",
      indent(
        (minMeasures ++ aggregatedDimensions)
          .mkString(s"${name}(", ", ", ")"),
        6
      ),
      s"    override def max(other: ${name})(groupBySet: Iterable[String]): ${name} =",
      "      val aggregated = e.aggregate(groupBySet)",
      indent(
        (maxMeasures ++ aggregatedDimensions)
          .mkString(s"${name}(", ", ", ")"),
        6
      )
    ).mkString("\n")

  /** Generate the code for an Event
    *
    * @param e
    *   the Event to generate
    * @return
    *   the generated code for the given Event
    */
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
      s"${event}",
      indent(generateGiven(e), 2)
    ).mkString("\n")
