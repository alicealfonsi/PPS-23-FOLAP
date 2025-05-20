package folap.typingDSL

import DimensionBuilder.dimension
import SeqBuilder.-->
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class DimensionBuilderSpec extends AnyFlatSpec with should.Matchers:
  "A DimensionBuilder" should "accept a dimension as a sequence of strings" in:
    val attributes = "town" --> "province" --> "region" --> "country"
    val geoMeasure = "geo" dimension attributes
    geoMeasure.name shouldEqual "geo"
    geoMeasure.attributes shouldEqual attributes
