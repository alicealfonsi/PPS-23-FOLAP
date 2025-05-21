package folap.core

import org.scalatest._

import MultidimensionalModel._
import flatspec._
import matchers._

class AttributeSpec extends AnyFlatSpec with should.Matchers:
  private case class ExampleAttribute() extends Attribute:
    override val value: String = ""
  val attribute: Attribute = ExampleAttribute()

  "An Attribute" should "have a name equal to the class name" in:
    attribute.name shouldEqual "ExampleAttribute"

  it should "have a descriptive value" in:
    attribute.value shouldEqual ""
