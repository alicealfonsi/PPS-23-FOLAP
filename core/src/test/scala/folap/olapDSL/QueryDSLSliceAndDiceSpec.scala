package folap.olapDSL

import folap.core.MultidimensionalModel._
import folap.core._
import folap.olapDSL.AttributeDSLBuilder._
import folap.olapDSL.AttributeSeqBuilder._
import folap.olapDSL.QueryDSLBuilder._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class QueryDSLSliceAndDiceSpec extends AnyFlatSpec with Matchers:

  import folap.core.CubeMockup._
  import CubeMockup.GeographicAttribute._
  import CubeMockup.ProductAttribute._

  val event1: SalesEvent = SalesEvent(
    Nation(None, "Italy"),
    Category(None, "Pizza"),
    QuantitySold(100)
  )

  val event2: SalesEvent = SalesEvent(
    Nation(None, "France"),
    Category(None, "Baguettes"),
    QuantitySold(150)
  )

  val event3: SalesEvent = SalesEvent(
    Nation(None, "Italy"),
    Category(None, "Pasta"),
    QuantitySold(120)
  )

  val events: Seq[SalesEvent] = Seq(event1, event2, event3)

  val Sales = QueryDSL[SalesAttributes, SalesAttribute, SalesMeasure](events)

  "sliceAndDice" should "filter events by a single attribute (slice)" in:
    val filtered = Sales where (Nation is "Italy")
    filtered.cube should contain theSameElementsAs Seq(event1, event3)

  it should "filter events by multiple attributes (dice)" in:
    val filter: Iterable[(SalesAttributes, String)] =
      (Nation is "Italy" and (Category is "Pizza"))
    val filtered = Sales where filter
    filtered.cube shouldEqual Seq(event1)

  it should "return all events when no filter is provided" in:
    val filtered = Sales
    filtered.cube should contain theSameElementsAs events

  it should "return an empty result when no event matches the filter" in:
    val filtered = Sales where (Nation is "Germany")
    filtered.cube shouldBe empty
