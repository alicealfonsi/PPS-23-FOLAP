package folap.core

import org.scalatest._

import MultidimensionalModel._
import flatspec._
import matchers._

class AttributeSpec extends AnyFlatSpec with should.Matchers:
  private case class ExampleAttribute() extends Attribute:
    override val parent: Option[Attribute] = Some(TopAttribute())
    override val value: String = ""
  private case class DimensionAttribute(
      override val parent: Option[Attribute],
      override val value: String
  ) extends Attribute
  val attribute: Attribute = ExampleAttribute()
  val dim: Attribute = DimensionAttribute(Some(attribute), "")

  "An Attribute" should "have a name equal to the class name" in:
    attribute.name shouldEqual "ExampleAttribute"

  it should "have a descriptive value" in:
    attribute.value shouldEqual ""

  it should "have an optional parent" in:
    attribute.parent shouldEqual Some(TopAttribute())

  it should "have a hierarchy" in:
    dim.hierarchy shouldEqual List(dim, attribute, TopAttribute())

  it should "be comparable: equal to" in:
    val other: Attribute =
      DimensionAttribute(Some(TopAttribute()), "")
    dim.equals(other) shouldBe true

  import CubeMockup.*, GeographicAttribute.*
  val shop: Shop = shop1
  it should "find the Attribute name in its hierarchy that matches one of the specified names if such an Attribute exists" in:
    shop.searchCorrespondingAttributeName(
      List("Category", "City")
    ) shouldEqual "City"

  it should "move up the hierarchy to the specified level" in:
    shop.upToLevel("City") shouldEqual City(Some(nation123), "Bologna")
