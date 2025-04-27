/** An Event is an instance of a fact that occurred in the business domain
  * @tparam A
  *   the type of the Event attributes
  * @tparam M
  *   the type of the Event measures
  */
trait Event[A <: EventAttribute, M <: EventMeasure[_]]:
  /** The dimensions that describe the Event
    * @return
    *   the list of Event dimensions
    */
  def dimensions: Iterable[A]

  /** The attributes that can be used to describe the Event
    * @return
    *   the list of Event attributes
    */
  def attributes: Iterable[A] =
    var attributes: Iterable[A] = List()
    dimensions.foreach(d =>
      attributes = attributes ++ d.hierarchy.asInstanceOf[Iterable[A]]
    )
    attributes

  /** The measures that quantify the Event
    * @return
    *   the list of Event measures
    */
  def measures: Iterable[M]
