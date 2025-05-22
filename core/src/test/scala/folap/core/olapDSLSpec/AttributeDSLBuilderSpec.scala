package folap.core.olapDSLSpec

import folap.core._
import folap.core.olapDSL.AttributeDSLBuilder._
import folap.core.olapDSL.AttributeSeqBuilder._
import folap.core.olapDSL._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class AttributeDSLBuilderSpec extends AnyFlatSpec with Matchers:

  "The is infix method" should "create an AttributeDSL with correct name and value" in:
    val attr = "Nation" is "Italy"

    attr shouldBe a[AttributeDSL]
    attr.name shouldEqual "NationAttribute"
    attr.value shouldEqual "Italy"

  "The is and is chain" should "create a Seq[AttributeDSL] with correct attributes" in:
    val result = "Nation" is "Italy" and ("Year" is "2024")

    result shouldBe a[Seq[_]]
    result should have size 2

    result shouldEqual Seq(
      AttributeDSL("Nation", "Italy"),
      AttributeDSL("Year", "2024")
    )
