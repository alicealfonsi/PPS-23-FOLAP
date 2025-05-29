package folap.core.olapDSLSpec
import folap.core.MultidimensionalModel._
import folap.core._
import folap.core.olapDSL.QueryDSL._
import folap.core.olapDSL.QueryDSLBuilder.union
import folap.core.olapDSL._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
class QueryDSLDrillAcrossSpec extends AnyFlatSpec with Matchers:
  trait SalesAttribute extends Attribute
  trait ProfitsAttribute extends Attribute
  trait CustomerAttribute extends Attribute
  trait SalesMeasure extends Measure
  trait ProfitsMeasure extends Measure
  trait CustomerMeasure extends Measure
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

  case class TotSalesMeasure(val value: Int) extends SalesMeasure:
    type T = Int
    override def fromRaw(value: Int): TotSalesMeasure = TotSalesMeasure(value)
  case class TotProfitsMeasure(val value: Int) extends ProfitsMeasure:
    type T = Int
    override def fromRaw(value: Int): TotProfitsMeasure = TotProfitsMeasure(
      value
    )
  case class TotPurchasesMeasure(val value: Int) extends CustomerMeasure:
    type T = Int
    override def fromRaw(value: Int): TotPurchasesMeasure =
      TotPurchasesMeasure(
        value
      )

  case class SalesEvent(
      override val dimensions: Iterable[SalesAttribute],
      override val measures: Iterable[SalesMeasure]
  ) extends Event[SalesAttribute, SalesMeasure]
  case class ProfitsEvent(
      override val dimensions: Iterable[ProfitsAttribute],
      override val measures: Iterable[ProfitsMeasure]
  ) extends Event[ProfitsAttribute, ProfitsMeasure]
  case class CustomerEvent(
      override val dimensions: Iterable[CustomerAttribute],
      override val measures: Iterable[CustomerMeasure]
  ) extends Event[CustomerAttribute, CustomerMeasure]
  case class ResultEvent[A <: Attribute, M <: Measure](
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

  given EventConstructor[A <: Attribute, M <: Measure]: EventConstructor[A, M] =
    (
        attributes: Iterable[A],
        measures: Iterable[M]
    ) => ResultEvent(attributes, measures)

  val Sales = QueryDSL(eventsA)
  val Profits = QueryDSL(eventsB)
  val Customers = QueryDSL(eventsC)

  "The DSL union" should "combine events with matching attributes" in:
    val result = Sales union Profits

    val expected = Seq(
      ResultEvent(
        Seq(NationAttribute("Italy", None)),
        Seq(TotSalesMeasure(100), TotProfitsMeasure(30))
      ),
      ResultEvent(
        Seq(NationAttribute("Italy", None)),
        Seq(TotSalesMeasure(120), TotProfitsMeasure(30))
      )
    )

    result.cube should have size 2
    result.cube should contain theSameElementsAs expected

  it should "return empty when no attributes match" in:
    val result = Sales union Customers
    result.cube shouldBe empty
