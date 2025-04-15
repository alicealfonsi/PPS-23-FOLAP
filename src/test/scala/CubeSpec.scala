import org.scalatest._

import flatspec._
import matchers._

class CubeSpec extends AnyFlatSpec with should.Matchers with BeforeAndAfterEach:
  case class ExampleCube() extends Cube.Cube