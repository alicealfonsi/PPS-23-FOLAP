package folap.core

import org.scalatest._

import flatspec._
import matchers._

class CubeSpec extends AnyFlatSpec with should.Matchers:
  private case class ExampleCube()
      extends Cube[ExampleEventAttribute, ExampleEventMeasure[_], ExampleEvent]:
    override def events: Iterable[ExampleEvent] = List(ExampleEvent())
  val cube: Cube[ExampleEventAttribute, ExampleEventMeasure[_], ExampleEvent] =
    ExampleCube()

  "A Cube" should "have a list of events" in:
    cube.events shouldEqual List(ExampleEvent())
