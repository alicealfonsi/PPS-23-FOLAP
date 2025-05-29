package folap.typingDSL

import folap.typingDSL.DSLUtils.indent
import folap.typingDSL.MeasureDSL.measure
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
        "  def parent = Some(folap.core.MultidimensionalModel.TopAttribute())"
      ).mkString("\n"),
      2
    )
    val generated = generate(geoMeasure)
    generated should endWith(expectedEnding)

  it should "generate a single attribute without parent for single level hierarchies" in:
    val geoDimension = "geo" dimension "shop"
    val expectedEnding = indent(
      Seq(
        "case class Shop(value: String) extends GeoDimension:",
        "  def parent = Some(folap.core.MultidimensionalModel.TopAttribute())"
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

  it should "generate a case class for a measure" in:
    val m = measure named "test" as Int
    val expected = Seq(
      "case class Test(value: Int) extends folap.core.MultidimensionalModel.Measure:",
      "  type T = Int",
      "  override def fromRaw(value: Int): folap.core.MultidimensionalModel.Measure =",
      "    Test(value)"
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

    generated should endWith(expectedEnding)
