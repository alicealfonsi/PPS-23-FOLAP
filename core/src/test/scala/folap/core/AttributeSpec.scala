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
