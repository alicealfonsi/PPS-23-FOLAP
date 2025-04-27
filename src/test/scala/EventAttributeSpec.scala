import MultidimensionalModel._
import org.scalatest._

import flatspec._
import matchers._

class EventAttributeSpec
    extends AnyFlatSpec
    with should.Matchers
    with BeforeAndAfterEach:

  private case class ParentAttribute(
      override val parent: Option[EventAttribute],
      override val value: String
  ) extends EventAttribute
  private case class DimensionAttribute(
      override val parent: Option[EventAttribute],
      override val value: String
  ) extends EventAttribute

  var parent: EventAttribute = ParentAttribute(Some(TopAttribute()), "")
  var dim: EventAttribute = DimensionAttribute(Some(parent), "")

  override protected def beforeEach(): Unit =
    parent = ParentAttribute(Some(TopAttribute()), "")
    dim = DimensionAttribute(Some(parent), "")

  "An EventAttribute" should "have an optional parent" in:
    parent.parent shouldEqual Some(TopAttribute())

  it should "have a hierarchy" in:
    dim.hierarchy shouldEqual List(dim, parent, TopAttribute())

  it should "be comparable: equal to" in:
    val dim2: EventAttribute = DimensionAttribute(Some(TopAttribute()), "")
    dim.equals(dim2) shouldBe true
