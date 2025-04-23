import MultidimensionalModel.Attribute
import MultidimensionalModel.Event
import MultidimensionalModel.Measure
import Operators.drillAcross
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
private case class CategoryAttribute(
    override val value: String,
    override val parent: Option[Attribute]
) extends Attribute;
private case class CustomerAttribute(
    override val value: String,
    override val parent: Option[Attribute]
) extends Attribute;
private case class TotSalesMeasure[T: Numeric](override val value: T)
    extends Measure[T]:
  override def fromRaw(value: T): Measure[T] = TotSalesMeasure(value)
private case class TotProfitsMeasure[T: Numeric](override val value: T)
    extends Measure[T]:
  override def fromRaw(value: T): Measure[T] = TotProfitsMeasure(value)
private case class TotPurchasesMeasure[T: Numeric](override val value: T)
    extends Measure[T]:
  override def fromRaw(value: T): Measure[T] = TotPurchasesMeasure(value)

private case class SalesEvent(
    override val attributes: Iterable[Attribute],
    override val measures: Iterable[Measure[_]]
) extends Event[Attribute, Measure[_]];
private case class ProfitsEvent(
    override val attributes: Iterable[Attribute],
    override val measures: Iterable[Measure[_]]
) extends Event[Attribute, Measure[_]];
private case class ResultEvent(
    override val attributes: Iterable[Attribute],
    override val measures: Iterable[Measure[_]]
) extends Event[Attribute, Measure[_]];
private case class CustomerEvent(
    override val attributes: Iterable[Attribute],
    override val measures: Iterable[Measure[_]]
) extends Event[Attribute, Measure[_]];

class SliceAndDiceSpec
    extends AnyFlatSpec
    with should.Matchers
    with BeforeAndAfterEach:

  val event1 = SalesEvent(
    attributes =
      Seq(NationAttribute("Italy", None), YearAttribute("2024", None)),
    measures = Seq(TotSalesMeasure(100))
  )

  val event2 = SalesEvent(
    attributes =
      Seq(NationAttribute("France", None), YearAttribute("2024", None)),
    measures = Seq(TotSalesMeasure(150))
  )

  val event3 = SalesEvent(
    attributes =
      Seq(NationAttribute("Italy", None), YearAttribute("2023", None)),
    measures = Seq(TotSalesMeasure(120))
  )

  val event4 = ProfitsEvent(
    attributes =
      Seq(NationAttribute("Italy", None), CategoryAttribute("shoes", None)),
    measures = Seq(TotProfitsMeasure(30))
  )
  val event5 = ProfitsEvent(
    attributes =
      Seq(NationAttribute("Spain", None), CategoryAttribute("bags", None)),
    measures = Seq(TotProfitsMeasure(40))
  )
  val event6 = CustomerEvent(
    Seq(CustomerAttribute("Claudia", None)),
    Seq(TotPurchasesMeasure(5))
  )

  val eventsA = Seq(event1, event2, event3)
  val eventsB = Seq(event4, event5)
  val eventsC = Seq(event6)

  def createEvent: EventConstructor[Attribute, Measure[_]] =
    (
        attributes: Iterable[Attribute],
        measures: Iterable[Measure[_]]
    ) => ResultEvent(attributes, measures)

  "drillAcross" should "combine events with matching attributes" in:
    val result = drillAcross(eventsA, eventsB, createEvent)
    val resultEvent1 = ResultEvent(
      Seq(NationAttribute("Italy", None)),
      Seq(TotSalesMeasure(100), TotProfitsMeasure(30))
    )
    val resultEvent2 = ResultEvent(
      Seq(NationAttribute("Italy", None)),
      Seq(TotSalesMeasure(120), TotProfitsMeasure(30))
    )
    val resultEvents = Seq(resultEvent1, resultEvent2)
    result should have size 2
    result should contain theSameElementsAs resultEvents

  it should "return empty when no attributes match" in:
    val result = drillAcross(eventsA, eventsC, createEvent)
    result should be(empty)
