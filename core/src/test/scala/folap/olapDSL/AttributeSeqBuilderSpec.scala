package folap.olapDSL

import folap.olapDSL.AttributeSeqBuilder._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import folap.core.CubeMockup.GeographicAttribute._
import folap.core.CubeMockup.ProductAttribute._

class AttributeSeqBuilderSpec extends AnyFlatSpec with Matchers:

  "An AttributeSeqBuilder" should "create a Seq from two AttributeDSL elements using and" in:
    val first = (Nation, "Italy")
    val second = (Category, "Pizza")
    val result = first and second
    result shouldEqual Seq(first, second)

  it should "create a Seq from a Seq and another AttributeDSL using and" in:
    val base = (Nation, "Italy") and (Category, "Pizza")
    val extended = base and (Product, "Shoes")
    extended shouldEqual Seq(
      AttributeDSL(Nation, "Italy"),
      AttributeDSL(Category, "Pizza"),
      AttributeDSL(Category, "Shoes")
    )
