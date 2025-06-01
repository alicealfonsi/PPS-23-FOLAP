package folap.core

import org.scalatest._

import scala.language.postfixOps

import MultidimensionalModel._
import Operators.drillAcross
import flatspec._
import matchers._

class DrillAcrossSpec
    extends AnyFlatSpec
    with should.Matchers
    with BeforeAndAfterEach:

  type AllAttributes = SalesAttributes | ProfitsAttributes | CustomerAttributes
  trait SalesAttribute extends Attribute[SalesAttributes | ProfitsAttributes]
  type SalesAttributes = NationAttribute.type | YearAttribute.type
  trait ProfitsAttribute extends Attribute[ProfitsAttributes | SalesAttributes]
  type ProfitsAttributes = CategoryAttribute.type
  trait CustomerAttribute extends Attribute[CustomerAttributes]
  type CustomerAttributes = CustomerNameAttribute.type
  trait SalesMeasure extends Measure
  trait ProfitsMeasure extends Measure
  trait CustomerMeasure extends Measure

  case class NationAttribute(
      override val value: String,
      override val parent: Option[
        Attribute[ProfitsAttributes | SalesAttributes]
      ]
  ) extends SalesAttribute
      with ProfitsAttribute:
    override val level = NationAttribute

  case class YearAttribute(
      override val value: String,
      override val parent: Option[
        Attribute[SalesAttributes | ProfitsAttributes]
      ]
  ) extends SalesAttribute:
    override val level = YearAttribute

  case class CategoryAttribute(
      override val value: String,
      override val parent: Option[
        Attribute[ProfitsAttributes | SalesAttributes]
      ]
  ) extends ProfitsAttribute:
    override val level = CategoryAttribute
  case class CustomerNameAttribute(
      override val value: String,
      override val parent: Option[Attribute[CustomerAttributes]]
  ) extends CustomerAttribute:
    override val level = CustomerNameAttribute

  case class TotSalesMeasure(override val value: Int) extends SalesMeasure:
    type T = Int

  case class TotProfitsMeasure(override val value: Double)
      extends ProfitsMeasure:
    type T = Double

  case class TotPurchasesMeasure(override val value: Int)
      extends CustomerMeasure:
    type T = Int

  case class SalesEvent(
      override val dimensions: Iterable[SalesAttribute],
      override val measures: Iterable[SalesMeasure]
  ) extends Event[
        SalesAttributes | ProfitsAttributes,
        SalesAttribute,
        SalesMeasure
      ]
  case class ProfitsEvent(
      override val dimensions: Iterable[ProfitsAttribute],
      override val measures: Iterable[ProfitsMeasure]
  ) extends Event[
        ProfitsAttributes | SalesAttributes,
        ProfitsAttribute,
        ProfitsMeasure
      ]
  case class CustomerEvent(
      override val dimensions: Iterable[CustomerAttribute],
      override val measures: Iterable[CustomerMeasure]
  ) extends Event[CustomerAttributes, CustomerAttribute, CustomerMeasure]
  case class ResultEvent[
      L <: SalesAttributes | ProfitsAttributes | CustomerAttributes,
      A <: Attribute[L],
      M <: Measure
  ](
      override val dimensions: Iterable[A],
      override val measures: Iterable[M]
  ) extends Event[L, A, M]

  val event1: SalesEvent = SalesEvent(
    dimensions =
      Seq(NationAttribute("Italy", None), YearAttribute("2024", None)),
    measures = Seq(TotSalesMeasure(100))
  )

  val event2: SalesEvent = SalesEvent(
    dimensions =
      Seq(NationAttribute("France", None), YearAttribute("2024", None)),
    measures = Seq(TotSalesMeasure(150))
  )

  val event3: SalesEvent = SalesEvent(
    dimensions =
      Seq(NationAttribute("Italy", None), YearAttribute("2023", None)),
    measures = Seq(TotSalesMeasure(120))
  )

  val event4: ProfitsEvent = ProfitsEvent(
    dimensions =
      Seq(NationAttribute("Italy", None), CategoryAttribute("shoes", None)),
    measures = Seq(TotProfitsMeasure(30))
  )
  val event5: ProfitsEvent = ProfitsEvent(
    dimensions =
      Seq(NationAttribute("Spain", None), CategoryAttribute("bags", None)),
    measures = Seq(TotProfitsMeasure(40))
  )
  val event6: CustomerEvent = CustomerEvent(
    Seq(CustomerNameAttribute("Claudia", None)),
    Seq(TotPurchasesMeasure(5))
  )

  val eventsA: Seq[SalesEvent] = Seq(event1, event2, event3)
  val eventsB: Seq[ProfitsEvent] = Seq(event4, event5)
  val eventsC: Seq[CustomerEvent] = Seq(event6)

  def createEvent: EventConstructor[
    NationAttribute | YearAttribute | CategoryAttribute,
    Attribute[NationAttribute | YearAttribute | CategoryAttribute],
    SalesMeasure | ProfitsMeasure
  ] = (
      attributes: Iterable[
        Attribute[NationAttribute | YearAttribute | CategoryAttribute]
      ],
      measures: Iterable[SalesMeasure | ProfitsMeasure]
  ) => ??? /* ResultEvent(Seq() /* attributes */, measures) */

  "drillAcross" should "combine events with matching attributes" in:
    val result = drillAcross(eventsA, eventsB, ??? /* createEvent */ )
    val resultEvent1 = ResultEvent(
      Seq( /* NationAttribute("Italy", None) */ ),
      Seq(TotSalesMeasure(100), TotProfitsMeasure(30))
    )
    val resultEvent2 = ResultEvent(
      Seq( /* NationAttribute("Italy", None) */ ),
      Seq(TotSalesMeasure(120), TotProfitsMeasure(30))
    )
    val resultEvents = Seq(resultEvent1, resultEvent2)
    result should have size 2
    result should contain theSameElementsAs resultEvents

  // Not needed?
  // it should "return empty when no attributes match" in:
  //   val result = drillAcross(eventsA, eventsC, createEvent)
  //   result should be(empty)
