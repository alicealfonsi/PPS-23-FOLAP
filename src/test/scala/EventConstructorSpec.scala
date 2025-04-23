import MultidimensionalModel.Event
import org.scalatest._

import flatspec._
import matchers._

private case class ResultEvent(
    override val attributes: Iterable[ExampleAttribute],
    override val measures: Iterable[ExampleMeasure[_]]
) extends Event[ExampleAttribute, ExampleMeasure[_]]

class EventConstructorSpec extends AnyFlatSpec with should.Matchers:
  val attributes: Iterable[ExampleAttribute] = List(ExampleAttribute())
  val measures: Iterable[ExampleMeasure[_]] = List(ExampleMeasure(10))
  def createEvent: EventConstructor[ExampleAttribute, ExampleMeasure[_]] =
    (
        attributes: Iterable[ExampleAttribute],
        measures: Iterable[ExampleMeasure[_]]
    ) => ResultEvent(attributes, measures)

  "An EventConstructor" should "create an Event with given attributes and measures" in:
    createEvent(attributes, measures) shouldEqual ResultEvent(
      attributes,
      measures
    )
