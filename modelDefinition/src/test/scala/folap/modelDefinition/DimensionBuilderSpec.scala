package folap.modelDefinition

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

import DimensionBuilder.dimension
import SeqBuilder.-->

class DimensionBuilderSpec extends AnyFlatSpec with should.Matchers:
  "A DimensionBuilder" should "accept a dimension as a sequence of strings" in:
    val attributes = "town" --> "province" --> "region" --> "country"
    val geoDimension = "geo" dimension attributes
    geoDimension.name shouldEqual "geo"
    geoDimension.attributes shouldEqual attributes
