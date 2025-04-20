import Cube.Event
import org.scalatest._

import scala.language.postfixOps

import flatspec._
import matchers._

class EventSpec
    extends AnyFlatSpec
    with should.Matchers
    with BeforeAndAfterEach:
  private case class ExampleEvent() extends Event[ExampleAttribute]:
    override val attributes: Iterable[ExampleAttribute] = Seq(
      ExampleAttribute()
    )
  var event: Event[ExampleAttribute] = ExampleEvent()
  override protected def beforeEach(): Unit =
    event = ExampleEvent()

  "An Event" should "have a list of attributes" in:
    event.attributes shouldEqual Seq(ExampleAttribute())

  it should "have at least one attribute" in:
    event.attributes.size should be >= 1
