package folap.core.olapDSLSpec

import folap.core.olapDSL.AttributeDSLBuilder._
import folap.core.olapDSL.AttributeSeqBuilder._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class AttributeSeqBuilderSpec extends AnyFlatSpec with Matchers:

  "An AttributeSeqBuilder" should "create a Seq from two AttributeDSL elements using and" in:
    val first = "Nation" is "Italy"
    val second = "Year" is "2024"
    val result = first and second
    result shouldEqual Seq(first, second)

  it should "create a Seq from a Seq and another AttributeDSL using and" in:
    val base = ("Nation" is "Italy") and ("Year" is "2024")
    val extended = base and ("Product" is "Shoes")
    extended shouldEqual Seq(
      "Nation" is "Italy",
      "Year" is "2024",
      "Product" is "Shoes"
    )
