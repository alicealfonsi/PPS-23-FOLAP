package folap.core

import org.scalatest._

import scala.language.postfixOps

import MultidimensionalModel._
import Operators.sliceAndDice
import flatspec._
import matchers._

class SliceAndDiceSpec
    extends AnyFlatSpec
    with should.Matchers
    with BeforeAndAfterEach:
  type SalesAttributes = NationAttribute.type | YearAttribute.type
  trait SalesAttribute extends Attribute[SalesAttributes]
  trait SalesMeasure extends Measure
  case class NationAttribute(
      override val value: String,
      override val parent: Option[Attribute[SalesAttributes]]
  ) extends SalesAttribute:
    override val level = NationAttribute

  case class YearAttribute(
      override val value: String,
      override val parent: Option[Attribute[SalesAttributes]]
  ) extends SalesAttribute:
    val level = YearAttribute

  case class TotSalesMeasure(override val value: Int) extends SalesMeasure:
    type T = Int

  case class SalesEvent(
      override val dimensions: Iterable[SalesAttribute],
      override val measures: Iterable[SalesMeasure]
  ) extends Event[SalesAttributes, SalesAttribute, SalesMeasure]

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

  val events: Seq[SalesEvent] = Seq(event1, event2, event3)

  "sliceAndDice" should "filter events by a single attribute (slice)" in:
    val filtered =
      sliceAndDice(events, Seq((NationAttribute, "Italy")))
    filtered should contain theSameElementsAs Seq(event1, event3)

  it should "filter events by multiple attributes (dice)" in:
    val filtered = sliceAndDice(
      events,
      Seq((NationAttribute, "Italy"), (YearAttribute, "2024"))
    )
    filtered should contain theSameElementsAs Seq(event1)

  it should "return all events when no filter is provided" in:
    val filtered = sliceAndDice(events, Seq.empty)
    filtered should contain theSameElementsAs events

  it should "return an empty result when no event matches the filter" in:
    val filtered =
      sliceAndDice(events, Seq((NationAttribute, "Germany")))
    filtered should be(empty)
