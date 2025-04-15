import Cube.Event
import org.scalatest._

import flatspec._
import matchers._

class EventSpec
    extends AnyFlatSpec
    with should.Matchers
    with BeforeAndAfterEach:
  case class ExampleEvent() extends Event
