package folap.typingDSL

/** The case class representing an Event
  * @param name
  *   the name of the Event
  * @param dimensions
  *   the dimensions of the Event
  * @param measures
  *   the measures of the Event
  */
case class Event(
    name: String,
    dimensions: Seq[Dimension],
    measures: Seq[Measure[_]]
):
  /** Sets a property of an empty Event
    * @param property
    *   the dimension or measure to set for the Event
    * @return
    *   a new Event with the property
    */
  def having(property: Dimension | Measure[_]): Event = property match
    case d: Dimension  => Event(name, dimensions :+ d, measures)
    case m: Measure[_] => Event(name, dimensions, measures :+ m)

  /** Appends a property of an Event to its sequence of properties of the same
    * type
    * @param property
    *   the dimension or measure to set for the Event
    * @return
    *   a new Event with the property
    */
  def and(property: Dimension | Measure[_]): Event = having(property)

/** Builder of an Event
  */
object EventBuilder:

  /** The case class representing an Event builder instance
    */
  case class EventWord():
    /** Creates an empty Event from a string representing its name
      * @param name
      *   the name to set for the Event
      * @return
      *   a new Event with the name
      */
    def named(name: String): Event = Event(name, Seq(), Seq())

  /** Entry point for building an Event using the DSL. Creates an Event builder
    * instance
    * @return
    *   a new EventWord instance to start the Event construction
    */
  def event: EventWord = EventWord()
