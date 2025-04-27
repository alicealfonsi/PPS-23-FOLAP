import MultidimensionalModel._
import org.scalatest._

import flatspec._
import matchers._

class AttributeSpec
    extends AnyFlatSpec
    with should.Matchers
    with BeforeAndAfterEach:

  private case class ExampleAttribute() extends Attribute:
    override val value: String = ""

  var attribute: Attribute = ExampleAttribute()

  override protected def beforeEach(): Unit =
    attribute = ExampleAttribute()

  "An Attribute" should "have a name equal to the class name" in:
    attribute.name shouldEqual "ExampleAttribute"

  it should "have a descriptive value" in:
    attribute.value shouldEqual ""
