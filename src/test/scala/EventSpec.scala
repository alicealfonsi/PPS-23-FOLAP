import Cube.Event
import org.scalatest._

import scala.language.postfixOps

import flatspec._
import matchers._

class EventSpec
    extends AnyFlatSpec
    with should.Matchers
    with BeforeAndAfterEach:
  private case class ExampleEvent()
      extends Event[ExampleAttribute, ExampleMeasure[_]]:
    override val attributes: Iterable[ExampleAttribute] = List(
      ExampleAttribute()
    )
    override val measures: Iterable[ExampleMeasure[_]] = List(
      ExampleMeasure(10)
    )
  var event: Event[ExampleAttribute, ExampleMeasure[_]] = ExampleEvent()
  override protected def beforeEach(): Unit =
    event = ExampleEvent()

  "An Event" should "have a list of attributes" in:
    event.attributes shouldEqual List(ExampleAttribute())

  it should "have at least one attribute" in:
    event.attributes.size should be >= 1

  it should "have a list of measures" in:
    event.measures shouldEqual List(ExampleMeasure(10))
