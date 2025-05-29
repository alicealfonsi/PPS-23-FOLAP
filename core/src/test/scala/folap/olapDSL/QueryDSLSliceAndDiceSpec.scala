package folap.olapDSL

import folap.core.MultidimensionalModel._
import folap.core._
import folap.olapDSL.AttributeDSLBuilder._
import folap.olapDSL.AttributeSeqBuilder._
import folap.olapDSL.QueryDSLBuilder._
import folap.olapDSL._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class QueryDSLSliceAndDiceSpec extends AnyFlatSpec with Matchers:

  trait SalesAttribute extends Attribute
  trait SalesMeasure extends Measure
  case class NationAttribute(
      override val value: String,
      override val parent: Option[TopAttribute]
  ) extends SalesAttribute
  case class YearAttribute(
      override val value: String,
      override val parent: Option[TopAttribute]
  ) extends SalesAttribute
  case class TotSalesMeasure(val value: Int) extends SalesMeasure:
    type T = Int
    override def fromRaw(value: Int): TotSalesMeasure = TotSalesMeasure(value)

  case class SalesEvent(
      override val dimensions: Iterable[SalesAttribute],
      override val measures: Iterable[SalesMeasure]
  ) extends Event[SalesAttribute, SalesMeasure]
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

  val Sales = QueryDSL(events)

  "sliceAndDice" should "filter events by a single attribute (slice)" in:
    val Nation = "Nation"
    val filtered = Sales where (Nation is "Italy")
    filtered.cube should contain theSameElementsAs Seq(event1, event3)

  it should "filter events by multiple attributes (dice)" in:
    val Nation = "Nation"
    val Year = "Year"
    val filtered = Sales where (Nation is "Italy" and (Year is "2024"))
    filtered.cube shouldEqual Seq(event1)

  it should "return all events when no filter is provided" in:
    val filtered = Sales
    filtered.cube should contain theSameElementsAs events

  it should "return an empty result when no event matches the filter" in:
    val Nation = "Nation"
    val filtered = Sales where (Nation is "Germany")
    filtered.cube shouldBe empty
