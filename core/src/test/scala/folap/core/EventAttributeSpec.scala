package folap.core
import org.scalatest._

import flatspec._
import matchers._
import MultidimensionalModel._

class EventAttributeSpec extends AnyFlatSpec with should.Matchers:
  private case class ParentAttribute(
      override val parent: Option[Attribute],
      override val value: String
  ) extends Attribute
  private case class DimensionAttribute(
      override val parent: Option[Attribute],
      override val value: String
  ) extends Attribute
  val parent: Attribute = ParentAttribute(Some(TopAttribute()), "")
  val dim: Attribute = DimensionAttribute(Some(parent), "")

  "An Attribute" should "have an optional parent" in:
    parent.parent shouldEqual Some(TopAttribute())

  it should "have a hierarchy" in:
    dim.hierarchy shouldEqual List(dim, parent, TopAttribute())

  it should "be comparable: equal to" in:
    val anotherDim: Attribute =
      DimensionAttribute(Some(TopAttribute()), "")
    dim.equals(anotherDim) shouldBe true
