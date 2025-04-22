import MultidimensionalModel.Cube
import org.scalatest._

import flatspec._
import matchers._

class CubeSpec extends AnyFlatSpec with should.Matchers with BeforeAndAfterEach:
  private case class ExampleCube()
      extends Cube[ExampleAttribute, ExampleMeasure[_], ExampleEvent]:
    override def events: Iterable[ExampleEvent] = List(ExampleEvent())

  var cube: Cube[ExampleAttribute, ExampleMeasure[_], ExampleEvent] =
    ExampleCube()
  override protected def beforeEach(): Unit =
    cube = ExampleCube()

  "A Cube" should "have a list of events" in:
    cube.events shouldEqual List(ExampleEvent())
