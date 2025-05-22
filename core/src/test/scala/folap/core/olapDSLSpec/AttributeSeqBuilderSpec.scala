package folap.core.olapDSLSpec

import folap.core._
import folap.core.olapDSL.AttributeSeqBuilder._
import folap.core.olapDSL._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class AttributeSeqBuilderSpec extends AnyFlatSpec with Matchers:

  "An AttributeSeqBuilder" should "create a Seq from two AttributeDSL elements using and" in:
    val first = AttributeDSL("Nation", "Italy")
    val second = AttributeDSL("Year", "2024")
    val result = first and second
    result shouldEqual Seq(first, second)

  it should "create a Seq from a Seq and another AttributeDSL using and" in:
    val base = AttributeDSL("Nation", "Italy") and AttributeDSL("Year", "2024")
    val extended = base and AttributeDSL("Product", "Shoes")
    extended shouldEqual Seq(
      AttributeDSL("Nation", "Italy"),
      AttributeDSL("Year", "2024"),
      AttributeDSL("Product", "Shoes")
    )
