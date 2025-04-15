import org.scalatest.*
import flatspec.*
import matchers.*

class CubeSpec extends AnyFlatSpec with should.Matchers with BeforeAndAfterEach:
  case class ExampleCube() extends Cube.Cube