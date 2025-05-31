package folap.core

import org.scalatest._

import flatspec._
import matchers._

class EventConstructorSpec extends AnyFlatSpec with should.Matchers:
  private case class ResultEvent(
      override val dimensions: Iterable[ExampleEventAttribute],
      override val measures: Iterable[ExampleEventMeasure]
  ) extends Event[ExampleEventAttribute, ExampleEventMeasure]
  val dimensions: Iterable[ExampleEventAttribute] = List(
    DimensionExampleAttribute(None, "")
  )
  val measureValue: Int = 10
  val measures: Iterable[ExampleEventMeasure] = List(
    QuantityExampleMeasure(measureValue)
  )
  private def createEvent: EventConstructor[
    ExampleEventAttribute,
    ExampleEventMeasure,
    ResultEvent
  ] =
    (
        dimensions: Iterable[ExampleEventAttribute],
        measures: Iterable[ExampleEventMeasure]
    ) => ResultEvent(dimensions, measures)

  "An EventConstructor" should "create an Event with given dimensions and measures" in:
    createEvent(dimensions, measures) shouldEqual ResultEvent(
      dimensions,
      measures
    )
