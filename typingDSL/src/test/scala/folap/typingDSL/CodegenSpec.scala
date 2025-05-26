package folap.typingDSL

import org.scalatest._

import flatspec._
import matchers._
import SeqBuilder.-->
import DimensionBuilder.dimension
import Codegen.generate

class CodegenSpec extends AnyFlatSpec with should.Matchers:
  "Code generation" should "generate a trait for a dimension" in:
    val attributes = "town" --> "province" --> "region" --> "country"
    val geoMeasure = "geo" dimension attributes
    generate(geoMeasure) should startWith(
      "sealed trait GeoDimension\nobject GeoDimension:"
    )

  "Code generation" should "generate a case class for each attribute" in:
    val attributes = "town" --> "province" --> "region" --> "country"
    val geoMeasure = "geo" dimension attributes
    generate(geoMeasure) should endWith(
      """
  case class Town(value: String, parent: Province)
  case class Province(value: String, parent: Region)
  case class Region(value: String, parent: Country)
  case class Country(value: String)"""
    )
