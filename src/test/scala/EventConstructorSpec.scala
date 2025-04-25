import MultidimensionalModel.Event
import org.scalatest._

import flatspec._
import matchers._

private case class ResultEvent(
    override val dimensions: Iterable[ExampleAttribute],
    override val measures: Iterable[ExampleMeasure[_]]
) extends Event[ExampleAttribute, ExampleMeasure[_]]

class EventConstructorSpec extends AnyFlatSpec with should.Matchers:
  val dimensions: Iterable[ExampleAttribute] = List(ExampleAttribute())
  val measures: Iterable[ExampleMeasure[_]] = List(ExampleMeasure(10))
  def createEvent: EventConstructor[ExampleAttribute, ExampleMeasure[_]] =
    (
        dimensions: Iterable[ExampleAttribute],
        measures: Iterable[ExampleMeasure[_]]
    ) => ResultEvent(dimensions, measures)

  "An EventConstructor" should "create an Event with given dimensions and measures" in:
    createEvent(dimensions, measures) shouldEqual ResultEvent(
      dimensions,
      measures
    )
