import Cube.Attribute

import org.scalatest.*
import flatspec.*
import matchers.*

class AttributeSpec extends AnyFlatSpec with should.Matchers {

  case class ExampleAttribute(override val parent: Option[Attribute], override val value: String) extends Attribute

  "An Attribute" should "have an optional parent" in {
    val attribute: Attribute = ExampleAttribute(parent = None, value = "")
    val parent: Option[Attribute] = attribute.parent
    assert(parent.isEmpty)
  }

  it should "have a descriptive value" in {
    val description = "value"
    val attribute: Attribute = ExampleAttribute(parent = None, value = description)
    assert(attribute.value == description)
  }
}