package folap.modelDefinition

import folap.modelDefinition.CodegenUtils.indent
import folap.modelDefinition.MeasureDSL.measure
import org.scalatest._

import flatspec._
import matchers._
import SeqBuilder.-->
import DimensionBuilder.dimension
import EventBuilder.event
import Codegen.generate

class CodegenSpec extends AnyFlatSpec with should.Matchers:
  "Code generation" should "generate a trait for a dimension" in:
    val attributes = "town" --> "province" --> "region" --> "country"
    val geoMeasure = "geo" dimension attributes
    generate(geoMeasure) should startWith(
      "sealed trait GeoDimension extends Dimension\nobject GeoDimension:"
    )

  it should "generate a case class for each attribute" in:
    val attributes = "town" --> "province" --> "region" --> "country"
    val geoMeasure = "geo" dimension attributes
    val expectedEnding = indent(
      Seq(
        "case class Town(value: String, province: Province) extends GeoDimension:",
        "  def parent = Some(province)",
        "case class Province(value: String, region: Region) extends GeoDimension:",
        "  def parent = Some(region)",
        "case class Region(value: String, country: Country) extends GeoDimension:",
        "  def parent = Some(country)",
        "case class Country(value: String) extends GeoDimension:",
        "  def parent = Some(TopAttribute())",
        "case class TopAttribute() extends GeoDimension:",
        "  def parent = None",
        "  def value = \"\""
      ).mkString("\n"),
      2
    )
    val generated = generate(geoMeasure)
    generated should endWith(expectedEnding)

  it should "generate a single attribute with a top attribute as a parent for single level hierarchies" in:
    val geoDimension = "geo" dimension "shop"
    val expectedEnding = indent(
      Seq(
        "case class Shop(value: String) extends GeoDimension:",
        "  def parent = Some(TopAttribute())",
        "case class TopAttribute() extends GeoDimension:",
        "  def parent = None",
        "  def value = \"\""
      ).mkString("\n"),
      2
    )
    val generated = generate(geoDimension)
    generated should endWith(expectedEnding)

  it should "generate the correct type string for ints" in:
    val t: MeasureType = Int
    generate(t) shouldEqual "Int"

  it should "generate the correct type string for longs" in:
    val t: MeasureType = Long
    generate(t) shouldEqual "Long"

  it should "generate the correct type string for floats" in:
    val t: MeasureType = Float
    generate(t) shouldEqual "Float"

  it should "generate the correct type string for doubles" in:
    val t: MeasureType = Double
    generate(t) shouldEqual "Double"

  it should "generate the correct type string for big ints" in:
    val t: MeasureType = BigInt
    generate(t) shouldEqual "BigInt"

  it should "generate the correct type string for big decimals" in:
    val t: MeasureType = BigDecimal
    generate(t) shouldEqual "BigDecimal"

  it should "generate a case class for a measure" in:
    val m = measure named "test" as Int
    val expected = Seq(
      "case class Test(value: Int) extends folap.core.MultidimensionalModel.Measure:",
      "  type T = Int"
    ).mkString("\n")
    val generated = generate(m)
    generated shouldEqual expected

  it should "generate an object for an event" in:
    val e = event named "test"
    generate(e) should startWith("object Test:")

  it should "generate a case class for each measure contained in the event" in:
    val m = measure named "test" as Int
    val e = event named "test" having m
    generate(e) should include(indent(generate(m), 2))

  it should "generate a union type for all measures contained in the event" in:
    val m1 = measure named "test" as Int
    val m2 = measure named "another test" as Double
    val e = event named "test" having m1 and m2

    val expected = "type Measures = Test | AnotherTest"
    val generated = generate(e)

    generated should include(expected)

  it should "generate an object containing the event dimensions" in:
    val geoDimension = "geo" dimension "shop"
    val e = event named "test" having geoDimension
    val expectedInclude = indent(
      Seq(
        "sealed trait Dimension extends folap.core.MultidimensionalModel.Attribute",
        "object Dimension:",
        indent(generate(geoDimension), 2)
      ).mkString("\n"),
      2
    )
    val generated = generate(e)

    generated should include(expectedInclude)

  it should "generate an event as a case class with all of its measures and dimensions as fields" in:
    val geoDimension = "geographic" dimension "shop" --> "town"
    val quantity = measure named "quantity" as Int
    val e = event named "sales" having geoDimension and quantity
    val expectedEnding = indent(
      Seq(
        "case class Sales(quantity: Quantity, geographic: Dimension.GeographicDimension) extends folap.core.Event[Dimension, Measures]:",
        "  def dimensions: Iterable[Dimension] = Seq(geographic)",
        "  def measures: Iterable[Measures] = Seq(quantity)"
      ).mkString("\n"),
      2
    )
    val generated = generate(e)

    generated should include(expectedEnding)

  it should "generate a union type for all attributes contained in the event" in:
    val geoMeasure =
      "geo" dimension "town" --> "province"
    val dateTimeMeasure = "date and time" dimension "hour" --> "day" --> "month"
    val e = event named "test" having geoMeasure and dateTimeMeasure

    val expected =
      "type Attributes = GeoDimension.Town | GeoDimension.Province | DateAndTimeDimension.Hour | DateAndTimeDimension.Day | DateAndTimeDimension.Month"
    val generated = generate(e)

    generated should include(expected)

  it should "generate Event givens" in:
    val geoDimension = "geographic" dimension "shop"
    val quantity = measure named "test" as Int
    val e = event named "example" having geoDimension and quantity
    val expectedEnding = indent(
      Seq(
        "given folap.core.Operational[Dimension, Measures, Example] with",
        "  extension (e: Example)",
        "    override def aggregate(groupBySet: Iterable[String]): Example =",
        "      Example(e.test, e.geographic.upToLevel(e.geographic.searchCorrespondingAttributeName(groupBySet)))",
        "    override def sum(other: Example)(groupBySet: Iterable[String]): Example =",
        "      val aggregated = e.aggregate(groupBySet)",
        "      Example(Test(aggregated.test.value + other.test.value), aggregated.geographic)",
        "    override def div(n: Int): Example =",
        "      Example(Test(e.test.value / n), e.geographic)",
        "    override def min(other: Example)(groupBySet: Iterable[String]): Example =",
        "      val aggregated = e.aggregate(groupBySet)",
        "      Example(Test(aggregated.test.value.min(other.test.value)), aggregated.geographic)",
        "    override def max(other: Example)(groupBySet: Iterable[String]): Example =",
        "      val aggregated = e.aggregate(groupBySet)",
        "      Example(Test(aggregated.test.value.max(other.test.value)), aggregated.geographic)"
      ).mkString("\n"),
      2
    )
    val generated = generate(e)

    generated should include(expectedEnding)
