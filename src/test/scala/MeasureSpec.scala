import Cube.Measure
import org.scalatest.*
import flatspec.*
import matchers.*

case class ExampleMeasure[T <: Cube.Number](override val value: T) extends Measure[T]

class MeasureSpec extends AnyFlatSpec with should.Matchers with BeforeAndAfterEach:
  "A Measure" should "accept an integer value" in:
    val value: Int = 42
    val measure = ExampleMeasure(value)
    measure.value shouldEqual value

  it should "accept a long value" in :
    val value: Long = 4242
    val measure = ExampleMeasure(value)
    measure.value shouldEqual value

  it should "accept a float value" in :
    val value: Float = 42.42
    val measure = ExampleMeasure(value)
    measure.value shouldEqual value
