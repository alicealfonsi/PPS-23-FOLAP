import MultidimensionalModel.Attribute
import MultidimensionalModel.Event
import MultidimensionalModel.Measure
import Operators.Operator
import org.scalatest._

import scala.language.postfixOps

import flatspec._
import matchers._

private case class NationAttribute(
    override val value: String,
    override val parent: Option[Attribute]
) extends Attribute;
private case class YearAttribute(
    override val value: String,
    override val parent: Option[Attribute]
) extends Attribute;
private case class SalesMeasure[T: Numeric](override val value: T)
    extends Measure[T]:
  override def fromRaw(value: T): Measure[T] = SalesMeasure(value)

private case class SalesEvent(
    override val attributes: Iterable[Attribute],
    override val measures: Iterable[Measure[_]]
) extends Event[Attribute, Measure[_]];

class SliceAndDiceSpec
    extends AnyFlatSpec
    with should.Matchers
    with BeforeAndAfterEach:

  var event1 = SalesEvent(
    attributes =
      Seq(NationAttribute("Italy", None), YearAttribute("2024", None)),
    measures = Seq(SalesMeasure(100))
  )

  var event2 = SalesEvent(
    attributes =
      Seq(NationAttribute("France", None), YearAttribute("2024", None)),
    measures = Seq(SalesMeasure(150))
  )

  var event3 = SalesEvent(
    attributes =
      Seq(NationAttribute("Italy", None), YearAttribute("2023", None)),
    measures = Seq(SalesMeasure(120))
  )

  var events = Seq(event1, event2, event3)

  "sliceAndDice" should "filter events by a single attribute (slice)" in:
    val filtered =
      Operator.sliceAndDice(events, Seq(NationAttribute("Italy", None)))
    filtered should contain theSameElementsAs Seq(event1, event3)

  it should "filter events by multiple attributes (dice)" in:
    val filtered = Operator.sliceAndDice(
      events,
      Seq(NationAttribute("Italy", None), YearAttribute("2024", None))
    )
    filtered should contain theSameElementsAs Seq(event1)

  it should "return all events when no filter is provided" in:
    val filtered = Operator.sliceAndDice(events, Seq.empty)
    filtered should contain theSameElementsAs events

  it should "return an empty result when no event matches the filter" in:
    val filtered = Operator.sliceAndDice(events, Seq(NationAttribute("Germany", None)))
    filtered should be (empty)


