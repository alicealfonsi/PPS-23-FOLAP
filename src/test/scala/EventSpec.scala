import Cube.Event
import org.scalatest.*
import flatspec.*
import matchers.*

class EventSpec extends AnyFlatSpec with should.Matchers with BeforeAndAfterEach:
  case class ExampleEvent() extends Event
