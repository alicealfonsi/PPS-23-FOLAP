import Cube.Attribute
import org.scalatest._

import flatspec._
import matchers._

private case class ExampleAttribute() extends Attribute:
  override val parent: Option[Attribute] = None
  override val value: String = ""

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

  it should "have a descriptive value" in:
    attribute.value shouldEqual ""
