import Cube.Event
import org.scalatest.*
import flatspec.*
import matchers.*

import scala.language.postfixOps

class EventSpec
    extends AnyFlatSpec
    with should.Matchers
    with BeforeAndAfterEach:
  case class ExampleEvent() extends Event[ExampleAttribute]:
    override val attributes: Iterable[(String, ExampleAttribute)] = Seq(("test", ExampleAttribute()))

  "An Event" should "have at least one attribute" in:
    val e: Event[ExampleAttribute] = ExampleEvent()
    val attributes: Iterable[(String, Cube.Attribute)] = e.attributes
    attributes.size should be >= 1
