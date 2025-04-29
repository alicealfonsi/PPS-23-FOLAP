import MultidimensionalModel.Measure
import Operators.sliceAndDice
import org.scalatest._

import scala.language.postfixOps

import flatspec._
import matchers._
private trait SalesAttribute extends EventAttribute
private trait SalesMeasure[T] extends EventMeasure[T]
private case class NationAttribute(
    override val value: String,
    override val parent: Option[TopAttribute]
) extends SalesAttribute

private case class YearAttribute(
    override val value: String,
    override val parent: Option[TopAttribute]
) extends SalesAttribute

private case class TotSalesMeasure[T: Numeric](val value: T)
    extends SalesMeasure[T]:
  override def fromRaw(value: T): TotSalesMeasure[T] = TotSalesMeasure(value)

private case class SalesEvent(
    override val dimensions: Iterable[SalesAttribute],
    override val measures: Iterable[SalesMeasure[_]]
) extends Event[SalesAttribute, SalesMeasure[_]]

class SliceAndDiceSpec
    extends AnyFlatSpec
    with should.Matchers
    with BeforeAndAfterEach:

  val event1 = SalesEvent(
    dimensions =
      Seq(NationAttribute("Italy", None), YearAttribute("2024", None)),
    measures = Seq(TotSalesMeasure(100))
  )

  val event2 = SalesEvent(
    dimensions =
      Seq(NationAttribute("France", None), YearAttribute("2024", None)),
    measures = Seq(TotSalesMeasure(150))
  )

  val event3 = SalesEvent(
    dimensions =
      Seq(NationAttribute("Italy", None), YearAttribute("2023", None)),
    measures = Seq(TotSalesMeasure(120))
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
