import Cube.Measure
import org.scalatest.*
import flatspec.*
import matchers.*

case class ExampleMeasure(override val value: Int) extends Measure

class MeasureSpec extends AnyFlatSpec with should.Matchers with BeforeAndAfterEach:
  "A Measure" should "accept an integer value" in:
    val value: Int = 42
    val measure = ExampleMeasure(value)
    measure.value shouldEqual value
