import MultidimensionalModel._
import org.scalatest._

import scala.language.postfixOps

import flatspec._
import matchers._

trait ExampleEventAttribute extends EventAttribute
trait ExampleEventMeasure[T] extends EventMeasure[T]

case class DimensionExampleAttribute(
    override val parent: Option[TopAttribute],
    override val value: String
) extends ExampleEventAttribute

case class QuantityExampleMeasure(value: Int) extends ExampleEventMeasure[Int]:
  override def fromRaw(value: Int): Measure[Int] = QuantityExampleMeasure(value)

private case class ExampleEvent()
    extends Event[ExampleEventAttribute, ExampleEventMeasure[_]]:
  override def dimensions: Iterable[ExampleEventAttribute] =
    List(DimensionExampleAttribute(Some(TopAttribute()), ""))
  override def measures: Iterable[ExampleEventMeasure[_]] = List(
    QuantityExampleMeasure(10)
  )

class EventSpec
    extends AnyFlatSpec
    with should.Matchers
    with BeforeAndAfterEach:

  var dim: DimensionExampleAttribute =
    DimensionExampleAttribute(Some(TopAttribute()), "")

  var event: Event[ExampleEventAttribute, ExampleEventMeasure[_]] =
    ExampleEvent()

  override protected def beforeEach(): Unit =
    dim = DimensionExampleAttribute(Some(TopAttribute()), "")
    event = ExampleEvent()

  "An Event" should "have a list of dimensions" in:
    event.dimensions shouldEqual List(dim)

  it should "have at least one dimension" in:
    event.dimensions.size should be >= 1

  it should "have a list of attributes" in:
    event.attributes shouldEqual List(dim, TopAttribute())

  it should "have a list of measures" in:
    event.measures shouldEqual List(QuantityExampleMeasure(10))
