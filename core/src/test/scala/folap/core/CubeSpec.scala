package folap.core

import org.scalatest._

import flatspec._
import matchers._

class CubeSpec extends AnyFlatSpec with should.Matchers:
  private case class ExampleCube()
      extends Cube[
        DimensionExampleAttribute.type,
        ExampleEventAttribute,
        ExampleEventMeasure,
        ExampleEvent
      ]:
    override def events: Iterable[ExampleEvent] = List(ExampleEvent())
  val cube: Cube[
    DimensionExampleAttribute.type,
    ExampleEventAttribute,
    ExampleEventMeasure,
    ExampleEvent
  ] =
    ExampleCube()

  "A Cube" should "have a list of events" in:
    cube.events shouldEqual List(ExampleEvent())

  "An Iterable of events" should "match the name of an Attribute if all of them contain the Attribute" in:
    import CubeMockup.*, Cube.*
    List(event1, event2).matchAttributeByName(
      GeographicAttribute.City
    ) shouldBe true
