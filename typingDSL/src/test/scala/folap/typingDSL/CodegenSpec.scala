package folap.typingDSL

import org.scalatest._

import flatspec._
import matchers._
import SeqBuilder.-->
import DimensionBuilder.dimension
import EventBuilder.event
import Codegen.generate
import folap.typingDSL.MeasureDSL.measure

class CodegenSpec extends AnyFlatSpec with should.Matchers:
  "Code generation" should "generate a trait for a dimension" in:
    val attributes = "town" --> "province" --> "region" --> "country"
    val geoMeasure = "geo" dimension attributes
    generate(geoMeasure) should startWith(
      "sealed trait GeoDimension\nobject GeoDimension:"
    )

  it should "generate a case class for each attribute" in:
    val attributes = "town" --> "province" --> "region" --> "country"
    val geoMeasure = "geo" dimension attributes
    generate(geoMeasure) should endWith(
      """
  case class Town(value: String, parent: Province) extends GeoDimension
  case class Province(value: String, parent: Region) extends GeoDimension
  case class Region(value: String, parent: Country) extends GeoDimension
  case class Country(value: String) extends GeoDimension"""
    )

  it should "generate a single attribute without parent for single level hierarchies" in:
    val geoDimension = "geo" dimension "shop"
    generate(geoDimension) should endWith(
      "case class Shop(value: String) extends GeoDimension"
    )

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
    generate(m) shouldEqual "case class Test(value: Int)"

  it should "generate an object for an event" in:
    val e = event named "test"
    generate(e) should startWith("object Test:")
