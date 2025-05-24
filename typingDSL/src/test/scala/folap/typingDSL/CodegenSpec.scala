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
