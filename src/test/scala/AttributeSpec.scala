import Cube.Attribute

import org.scalatest.*
import flatspec.*
import matchers.*

class AttributeSpec extends AnyFlatSpec with should.Matchers:

  "An Attribute" should "have an optional parent" in new Attribute:
    override val value: String = ""
    val parent: Option[Attribute] = parent
    assert(parent.isEmpty)

  it should "have a descriptive value" in new Attribute:
    val description = "value"
    override val value: String = description
    override val parent: Option[Attribute] = None
    value shouldBe description