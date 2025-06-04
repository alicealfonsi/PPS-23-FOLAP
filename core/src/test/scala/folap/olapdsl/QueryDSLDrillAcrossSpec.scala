package folap.olapdsl
import folap.core._
import folap.core.multidimensionalmodel.Attribute
import folap.core.multidimensionalmodel.Measure
import folap.olapdsl.QueryDSLBuilder.union
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class QueryDSLDrillAcrossSpec extends AnyFlatSpec with Matchers:
  trait SalesAttribute extends Attribute
  trait ProfitsAttribute extends Attribute
  trait CustomerAttribute extends Attribute
  trait SalesMeasure extends Measure
  trait ProfitsMeasure extends Measure
  trait CustomerMeasure extends Measure
  case class TopAttribute() extends Attribute:
    override val parent: Option[Attribute] = None
    override val value: String = ""
  case class Nation(
      override val value: String,
      override val parent: Option[TopAttribute]
  ) extends SalesAttribute
      with ProfitsAttribute

  case class Year(
      override val value: String,
      override val parent: Option[TopAttribute]
  ) extends SalesAttribute

  case class Category(
      override val value: String,
      override val parent: Option[TopAttribute]
  ) extends ProfitsAttribute
  case class CustomerName(
      override val value: String,
      override val parent: Option[TopAttribute]
  ) extends CustomerAttribute

  case class TotSalesMeasure(override val value: Int) extends SalesMeasure:
    type T = Int
  case class TotProfitsMeasure(override val value: Int) extends ProfitsMeasure:
    type T = Int
  case class TotPurchasesMeasure(override val value: Int)
      extends CustomerMeasure:
    type T = Int

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
  val event1: SalesEvent = SalesEvent(
    dimensions = Seq(Nation("Italy", None), Year("2024", None)),
    measures = Seq(TotSalesMeasure(100))
  )

  val event2: SalesEvent = SalesEvent(
    dimensions = Seq(Nation("France", None), Year("2024", None)),
    measures = Seq(TotSalesMeasure(150))
  )

  val event3: SalesEvent = SalesEvent(
    dimensions = Seq(Nation("Italy", None), Year("2023", None)),
    measures = Seq(TotSalesMeasure(120))
  )

  val event4: ProfitsEvent = ProfitsEvent(
    dimensions = Seq(Nation("Italy", None), Category("shoes", None)),
    measures = Seq(TotProfitsMeasure(30))
  )
  val event5: ProfitsEvent = ProfitsEvent(
    dimensions = Seq(Nation("Spain", None), Category("bags", None)),
    measures = Seq(TotProfitsMeasure(40))
  )
  val event6: CustomerEvent = CustomerEvent(
    Seq(CustomerName("Claudia", None)),
    Seq(TotPurchasesMeasure(5))
  )

  val eventsA: Seq[SalesEvent] = Seq(event1, event2, event3)
  val eventsB: Seq[ProfitsEvent] = Seq(event4, event5)
  val eventsC: Seq[CustomerEvent] = Seq(event6)

  given EventConstructor[A <: Attribute, M <: Measure, E <: Event[A, M]]
      : EventConstructor[A, M, E] =
    (
        attributes: Iterable[A],
        measures: Iterable[M]
    ) => ResultEvent(attributes, measures).asInstanceOf[E]

  val Sales = QueryDSL(eventsA)
  val Profits = QueryDSL(eventsB)
  val Customers = QueryDSL(
    eventsC
  )

  "The DSL method `union`" should "combine events with matching attributes" in:
    val result = Sales union Profits

    val expected = Seq(
      ResultEvent(
        Seq(Nation("Italy", None)),
        Seq(TotSalesMeasure(100), TotProfitsMeasure(30))
      ),
      ResultEvent(
        Seq(Nation("Italy", None)),
        Seq(TotSalesMeasure(120), TotProfitsMeasure(30))
      )
    )

    result.cube should have size 2
    result.cube should contain theSameElementsAs expected

  it should "return empty when no attributes match" in:
    val result = Sales union Customers
    result.cube shouldBe empty
