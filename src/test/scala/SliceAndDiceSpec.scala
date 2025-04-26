import MultidimensionalModel.Attribute
import MultidimensionalModel.Event
import MultidimensionalModel.Measure
import Operators.sliceAndDice
import org.scalatest._

import scala.language.postfixOps

import flatspec._
import matchers._
private abstract class SalesAttribute(
    override val value: String,
    override val parent: Option[Attribute]
) extends Attribute

private case class NationAttribute(
    override val value: String,
    override val parent: Option[SalesAttribute]
) extends SalesAttribute(value, parent)

private case class YearAttribute(
    override val value: String,
    override val parent: Option[SalesAttribute]
) extends SalesAttribute(value, parent)

private abstract class SalesMeasure[T: Numeric](val value: T) extends Measure[T]

private case class totSalesMeasure[T: Numeric](override val value: T)
    extends SalesMeasure[T](value):
  override def fromRaw(value: T): SalesMeasure[T] = totSalesMeasure(value)

private case class SalesEvent(
    override val attributes: Iterable[SalesAttribute],
    override val measures: Iterable[SalesMeasure[_]]
) extends Event[SalesAttribute, SalesMeasure[_]]

class SliceAndDiceSpec
    extends AnyFlatSpec
    with should.Matchers
    with BeforeAndAfterEach:

  val event1 = SalesEvent(
    attributes =
      Seq(NationAttribute("Italy", None), YearAttribute("2024", None)),
    measures = Seq(totSalesMeasure(100))
  )

  val event2 = SalesEvent(
    attributes =
      Seq(NationAttribute("France", None), YearAttribute("2024", None)),
    measures = Seq(totSalesMeasure(150))
  )

  val event3 = SalesEvent(
    attributes =
      Seq(NationAttribute("Italy", None), YearAttribute("2023", None)),
    measures = Seq(totSalesMeasure(120))
  )

  val events = Seq(event1, event2, event3)

  "sliceAndDice" should "filter events by a single attribute (slice)" in:
    val filtered =
      sliceAndDice(events, Seq(NationAttribute("Italy", None)))
    filtered should contain theSameElementsAs Seq(event1, event3)

  it should "filter events by multiple attributes (dice)" in:
    val filtered = sliceAndDice(
      events,
      Seq(NationAttribute("Italy", None), YearAttribute("2024", None))
    )
    filtered should contain theSameElementsAs Seq(event1)

  it should "return all events when no filter is provided" in:
    val filtered = sliceAndDice(events, Seq.empty)
    filtered should contain theSameElementsAs events

  it should "return an empty result when no event matches the filter" in:
    val filtered =
      sliceAndDice(events, Seq(NationAttribute("Germany", None)))
    filtered should be(empty)
