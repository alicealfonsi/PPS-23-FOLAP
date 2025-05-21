package folap.typingDSL

case class Event(
    name: String,
    dimensions: Seq[Dimension],
    measures: Seq[Measure[_]]
):
  def having(dimension: Dimension): Event =
    Event(name, dimensions :+ dimension, measures)

object EventBuilder:
  case class EventWord():
    def named(name: String): Event = Event(name, Seq(), Seq())
  def event: EventWord = EventWord()
