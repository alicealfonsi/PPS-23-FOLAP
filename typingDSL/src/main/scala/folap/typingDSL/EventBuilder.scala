package folap.typingDSL

case class Event(
    name: String,
    dimensions: Seq[Dimension],
    measures: Seq[Measure[_]]
):
  def having(dimension: Dimension): Event =
    Event(name, dimensions :+ dimension, measures)
  def having(measure: Measure[_]): Event =
    Event(name, dimensions, measures :+ measure)
  def and(dimension: Dimension): Event = having(dimension)

object EventBuilder:
  case class EventWord():
    def named(name: String): Event = Event(name, Seq(), Seq())
  def event: EventWord = EventWord()
