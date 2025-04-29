import Operators.drillAcross
import org.scalatest._

import scala.language.postfixOps

import flatspec._
import matchers._

class DrillAcrossSpec
    extends AnyFlatSpec
    with should.Matchers
    with BeforeAndAfterEach:

  trait SalesAttribute extends EventAttribute
  trait ProfitsAttribute extends EventAttribute
  trait CustomerAttribute extends EventAttribute
  trait SalesMeasure[T] extends EventMeasure[T]
  trait ProfitsMeasure[T] extends EventMeasure[T]
  trait CustomerMeasure[T] extends EventMeasure[T]

  case class NationAttribute(
      override val value: String,
      override val parent: Option[TopAttribute]
  ) extends SalesAttribute
      with ProfitsAttribute

  case class YearAttribute(
      override val value: String,
      override val parent: Option[TopAttribute]
  ) extends SalesAttribute

  case class CategoryAttribute(
      override val value: String,
      override val parent: Option[TopAttribute]
  ) extends ProfitsAttribute
  case class CustomerNameAttribute(
      override val value: String,
      override val parent: Option[TopAttribute]
  ) extends CustomerAttribute

  case class TotSalesMeasure[T: Numeric](val value: T) extends SalesMeasure[T]:
    override def fromRaw(value: T): TotSalesMeasure[T] = TotSalesMeasure(value)
  case class TotProfitsMeasure[T: Numeric](val value: T)
      extends ProfitsMeasure[T]:
    override def fromRaw(value: T): TotProfitsMeasure[T] = TotProfitsMeasure(
      value
    )
  case class TotPurchasesMeasure[T: Numeric](val value: T)
      extends CustomerMeasure[T]:
    override def fromRaw(value: T): TotPurchasesMeasure[T] =
      TotPurchasesMeasure(
        value
      )

  case class SalesEvent(
      override val dimensions: Iterable[SalesAttribute],
      override val measures: Iterable[SalesMeasure[_]]
  ) extends Event[SalesAttribute, SalesMeasure[_]]
  case class ProfitsEvent(
      override val dimensions: Iterable[ProfitsAttribute],
      override val measures: Iterable[ProfitsMeasure[_]]
  ) extends Event[ProfitsAttribute, ProfitsMeasure[_]]
  case class CustomerEvent(
      override val dimensions: Iterable[CustomerAttribute],
      override val measures: Iterable[CustomerMeasure[_]]
  ) extends Event[CustomerAttribute, CustomerMeasure[_]]
  case class ResultEvent[A <: EventAttribute, M <: EventMeasure[_]](
      override val dimensions: Iterable[A],
      override val measures: Iterable[M]
  ) extends Event[A, M]

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

  val event4 = ProfitsEvent(
    dimensions =
      Seq(NationAttribute("Italy", None), CategoryAttribute("shoes", None)),
    measures = Seq(TotProfitsMeasure(30))
  )
  val event5 = ProfitsEvent(
    dimensions =
      Seq(NationAttribute("Spain", None), CategoryAttribute("bags", None)),
    measures = Seq(TotProfitsMeasure(40))
  )
  val event6 = CustomerEvent(
    Seq(CustomerNameAttribute("Claudia", None)),
    Seq(TotPurchasesMeasure(5))
  )

  val eventsA = Seq(event1, event2, event3)
  val eventsB = Seq(event4, event5)
  val eventsC = Seq(event6)

  def createEvent[A <: EventAttribute, M <: EventMeasure[_]]
      : EventConstructor[A, M] =
    (
        attributes: Iterable[A],
        measures: Iterable[M]
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
