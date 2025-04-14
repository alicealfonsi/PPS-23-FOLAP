import Cube.Attribute

import collection.mutable.Stack
import org.scalatest.*
import flatspec.*
import matchers.*

class AttributeSpec extends AnyFlatSpec with should.Matchers {

  case class ExampleAttribute(override val parent: Option[Attribute]) extends Attribute

  "An Attribute" should "have an optional parent" in {
    val attribute: Attribute = ExampleAttribute(parent = None)
    val parent: Option[Attribute] = attribute.parent
    assert(parent.isEmpty)
  }
}