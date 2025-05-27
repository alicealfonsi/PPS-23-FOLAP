package folap.core.olapDSLSpec

import folap.core._
import folap.core.olapDSL.AttributeDSLBuilder._
import folap.core.olapDSL.AttributeSeqBuilder._
import folap.core.olapDSL._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class AttributeDSLBuilderSpec extends AnyFlatSpec with Matchers:

  "The is infix method" should "create an AttributeDSL with correct name and value" in:
    val Nation = "Nation"
    val attribute = Nation is "Italy"

    attribute shouldBe a[AttributeDSL]
    attribute.name shouldEqual "NationAttribute"
    attribute.value shouldEqual "Italy"
    attribute shouldEqual AttributeDSL(Nation, "Italy")

  "The is and is chain" should "create a Seq[AttributeDSL] with correct attributes" in:
    val Nation = "Nation"
    val Year = "Year"
    val attributes = Nation is "Italy" and (Year is "2024")

    attributes shouldBe a[Seq[_]]
    attributes should have size 2

    attributes shouldEqual Seq(
      AttributeDSL(Nation, "Italy"),
      AttributeDSL(Year, "2024")
    )
  it should "create a Seq[AttributeDSL] from two attributes defined only by name" in:
    val Region = "Region"
    val Year = "Year"
    val attributes = Region and Year
    attributes shouldEqual Seq(
      AttributeDSL(Region, ""),
      AttributeDSL(Year, "")
    )
