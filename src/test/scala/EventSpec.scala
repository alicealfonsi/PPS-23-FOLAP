import MultidimensionalModel.Event
import org.scalatest._

import scala.language.postfixOps

import flatspec._
import matchers._

private case class ExampleEvent()
    extends Event[ExampleAttribute, ExampleMeasure[_]]:
  override val dimensions: Iterable[ExampleAttribute] = List(
    ExampleAttribute()
  )
  override val measures: Iterable[ExampleMeasure[_]] = List(
    ExampleMeasure(10)
  )

class EventSpec
    extends AnyFlatSpec
    with should.Matchers
    with BeforeAndAfterEach:
  var event: Event[ExampleAttribute, ExampleMeasure[_]] = ExampleEvent()
  override protected def beforeEach(): Unit =
    event = ExampleEvent()

  "An Event" should "have a list of dimensions" in:
    event.dimensions shouldEqual List(ExampleAttribute())

  it should "have at least one dimension" in:
    event.dimensions.size should be >= 1

  it should "have a list of measures" in:
    event.measures shouldEqual List(ExampleMeasure(10))
