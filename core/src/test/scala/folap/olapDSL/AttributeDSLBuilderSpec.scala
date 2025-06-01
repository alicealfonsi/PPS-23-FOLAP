package folap.olapDSL

import folap.olapDSL.AttributeDSL.AttributeDSL
import folap.olapDSL.AttributeDSLBuilder._
import folap.olapDSL.AttributeSeqBuilder._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import folap.core.CubeMockup.GeographicAttribute.Nation
import folap.core.CubeMockup.ProductAttribute.Category

class AttributeDSLBuilderSpec extends AnyFlatSpec with Matchers:

  "The is infix method" should "create an AttributeDSL with correct name and value" in:
    val attribute = Nation is "Italy"

    attribute shouldBe a[(Nation.type, String)]
    attribute._1 shouldEqual Nation
    attribute._2 shouldEqual "Italy"
    attribute shouldEqual AttributeDSL(Nation, "Italy")

  "The is and is chain" should "create a Seq[AttributeDSL] with correct attributes" in:
    val attributes = Nation is "Italy" and (Category is "Pizza")

    attributes shouldBe a[Seq[_]]
    attributes should have size 2

    attributes shouldEqual Seq(
      AttributeDSL(Nation, "Italy"),
      AttributeDSL(Category, "Pizza")
    )
  it should "create a Seq[AttributeDSL] from two attributes defined only by name" in:
    val Region = "Region"
    val Year = "Year"
    val attributes = Region and Year
    attributes shouldEqual Seq(
      AttributeDSL(Region),
      AttributeDSL(Year)
    )
