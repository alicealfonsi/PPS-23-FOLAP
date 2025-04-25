import MultidimensionalModel._
import org.scalatest._

import flatspec._
import matchers._

private case class ExampleAttribute() extends Attribute:
  override val parent: Option[Attribute] = None
  override val value: String = ""

private case class OtherAttribute(
    override val parent: Option[Attribute],
    override val value: String
) extends Attribute

private case class HierarchyAttribute(
    override val parent: Option[Attribute],
    override val value: String
) extends Attribute

class AttributeSpec
    extends AnyFlatSpec
    with should.Matchers
    with BeforeAndAfterEach:
  var attribute: Attribute = ExampleAttribute()
  override protected def beforeEach(): Unit =
    attribute = ExampleAttribute()

  "An Attribute" should "have an optional parent" in:
    val parent: Option[Attribute] = attribute.parent
    parent shouldEqual None

  it should "have a name equal to the class name" in:
    attribute.name shouldEqual "ExampleAttribute"

  it should "have a descriptive value" in:
    attribute.value shouldEqual ""

  it should "be comparable: equal to" in:
    val other1: Attribute = OtherAttribute(None, "")
    val other2: Attribute = OtherAttribute(Some(ExampleAttribute()), "")
    other1.equals(other2) shouldBe true

  it should "have a hierarchy" in:
    val grandParent: HierarchyAttribute =
      HierarchyAttribute(Some(TopAttribute()), "GrandParentAttribute")
    val parent: HierarchyAttribute =
      HierarchyAttribute(Some(grandParent), "ParentAttribute")
    val dim: HierarchyAttribute =
      HierarchyAttribute(Some(parent), "DimensionAttribute")
    dim.hierarchy shouldEqual List(parent, grandParent, TopAttribute())
