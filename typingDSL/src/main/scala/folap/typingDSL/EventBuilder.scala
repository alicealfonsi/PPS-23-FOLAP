package folap.typingDSL

case class Event(
    name: String,
    dimensions: Seq[Dimension],
    measures: Seq[Measure[_]]
):
  def having(property: Dimension | Measure[_]): Event = property match
    case d: Dimension  => Event(name, dimensions :+ d, measures)
    case m: Measure[_] => Event(name, dimensions, measures :+ m)
  def and(property: Dimension | Measure[_]): Event = having(property)

object EventBuilder:
  case class EventWord():
    def named(name: String): Event = Event(name, Seq(), Seq())
  def event: EventWord = EventWord()
