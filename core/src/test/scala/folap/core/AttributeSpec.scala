package folap.core

import org.scalatest._

import MultidimensionalModel._
import flatspec._
import matchers._

class AttributeSpec extends AnyFlatSpec with should.Matchers:
  private case class ExampleAttribute()
      extends Attribute[
        ExampleAttribute.type | DimensionAttribute.type
      ]:
    override val parent: Option[Attribute[
      ExampleAttribute.type | DimensionAttribute.type
    ]] =
      None
    override val value: String = ""
    override val level: ExampleAttribute.type = ExampleAttribute
  private case class DimensionAttribute(
      override val parent: Option[
        Attribute[
          ExampleAttribute.type | DimensionAttribute.type
        ]
      ],
      override val value: String
  ) extends Attribute[
        ExampleAttribute.type | DimensionAttribute.type
      ]:
    override val level = DimensionAttribute

  private val attribute: Attribute[
    ExampleAttribute.type | DimensionAttribute.type
  ] =
    ExampleAttribute()
  private val dim: Attribute[
    ExampleAttribute.type | DimensionAttribute.type
  ] =
    DimensionAttribute(Some(attribute), "")

  "An Attribute" should "have a name equal to the class name" in:
    attribute.name shouldEqual "ExampleAttribute"

  it should "have a descriptive value" in:
    attribute.value shouldEqual ""

  it should "have an optional parent" in:
    attribute.parent shouldEqual None

  it should "have a hierarchy" in:
    dim.hierarchy shouldEqual List(dim, attribute)

  it should "be comparable: equal to" in:
    val other: Attribute[
      ExampleAttribute.type | DimensionAttribute.type
    ] =
      DimensionAttribute(None, "")
    dim.equals(other) shouldBe true

  import CubeMockup.*, GeographicAttribute.*
  val shop: Shop = shop1

  it should "move up the hierarchy to the specified level" in:
    shop.upToLevel(City) shouldEqual City(Some(nation123), "Bologna")
