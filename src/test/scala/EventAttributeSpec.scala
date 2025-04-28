import MultidimensionalModel._
import org.scalatest._

import flatspec._
import matchers._

class EventAttributeSpec extends AnyFlatSpec with should.Matchers:
  private case class ParentAttribute(
      override val parent: Option[EventAttribute],
      override val value: String
  ) extends EventAttribute
  private case class DimensionAttribute(
      override val parent: Option[EventAttribute],
      override val value: String
  ) extends EventAttribute
  val parent: EventAttribute = ParentAttribute(Some(TopAttribute()), "")
  val dim: EventAttribute = DimensionAttribute(Some(parent), "")

  "An EventAttribute" should "have an optional parent" in:
    parent.parent shouldEqual Some(TopAttribute())

  it should "have a hierarchy" in:
    dim.hierarchy shouldEqual List(dim, parent, TopAttribute())

  it should "be comparable: equal to" in:
    val anotherDim: EventAttribute =
      DimensionAttribute(Some(TopAttribute()), "")
    dim.equals(anotherDim) shouldBe true
