package folap.core

import MultidimensionalModel._

/** Operators for querying and manipulating events of a multidimensional data
  * warehouse
  */
object Operators:

  /** "Drill across" operation used to combine events from different cubes that
    * share common attribute names and values.
    *
    * @param events
    *   first set of events
    * @param otherEvents
    *   second set of events to combine with
    * @param createEvent
    *   function to construct a new event from shared attributes and combined
    *   measures
    * @tparam A1
    *   the type of attributes in the first set of events
    * @tparam M1
    *   the type of measures in the first set of events
    * @tparam A2
    *   the type of attributes in the second set of events
    * @tparam M2
    *   the type of measures in the second set of events
    * @return
    *   set of events resulting from the combination of matching events from
    *   both sets
    */
  def drillAcross[
      A1 <: Attribute,
      M1 <: Measure,
      A2 <: Attribute,
      M2 <: Measure
  ](
      events: Iterable[Event[A1, M1]],
      otherEvents: Iterable[Event[A2, M2]],
      createEvent: EventConstructor[Attribute, M1 | M2]
  ): Iterable[Event[Attribute, M1 | M2]] = {

    def leafAttributes(attrs: Iterable[Attribute]): Iterable[Attribute] =
      val allParents = attrs.flatMap(_.parent)
      attrs.filterNot(a => allParents.exists(_ == a))

    events.flatMap { eventA =>
      val leavesA = leafAttributes(eventA.attributes)

      otherEvents.flatMap { eventB =>
        val leavesB = leafAttributes(eventB.attributes)

        val commonLeaves = leavesA.filter { attrA =>
          leavesB.exists(attrB =>
            attrA.name == attrB.name && attrA.value == attrB.value
          )
        }

        if (commonLeaves.nonEmpty) {
          val combinedMeasures: Iterable[M1 | M2] =
            eventA.measures ++ eventB.measures
          Seq(createEvent(commonLeaves, combinedMeasures))
        } else {
          Nil
        }
      }
    }
  }

  /** "Slice and dice" operation used to filter events by matching attribute
    * names and values.
    *
    * @param events
    *   events to filter
    * @param filters
    *   attributes to match by name and value
    * @tparam A
    *   type of attributes
    * @tparam M
    *   type of measures
    * @return
    *   events that match all filters
    */

  def sliceAndDice[A <: Attribute, M <: Measure](
      events: Iterable[Event[A, M]],
      filters: Iterable[Attribute]
  ): Iterable[Event[A, M]] =
    events.filter { event =>
      filters.forall { filter =>
        event.attributes
          .find(attr => attr.name == filter.name)
          .exists(_.value == filter.value)
      }

    }

  def rollUp[A <: Attribute, M <: Measure](
      events: Iterable[Event[A, M]]
  )(
      groupBySet: Iterable[String]
  )(createEvent: EventConstructor[A, M]): Iterable[Event[A, M]] =
    if groupBySet.exists(name => events.matchAttributeByName(name))
    then
      val groupByAttributesNames =
        groupBySet.filter(name => events.matchAttributeByName(name))
      val groupByMap =
        events.groupBy(
          _.findAttributesByNames(groupByAttributesNames).map(_.value)
        )
      val groupByDimensions = groupByMap.values.map(
        _.head.findAttributesByNames(groupByAttributesNames)
      )
      val otherDimensions =
        groupByMap.values.head.head.dimensions
          .filter(
            _.hierarchy.forall(a =>
              groupByAttributesNames.forall(
                _ != a.name
              )
            )
          )
          .map(_ => events.head.topAttribute)
      groupByDimensions
        .map(d => d ++ otherDimensions)
        .map(dimensions => createEvent(dimensions, List()))
    else events
  extension [A <: Attribute, M <: Measure](event: Event[A, M])
    /** Finds the attributes of this event whose name is equal to one of the
      * specified names
      * @param names
      *   the names of the attributes to be found
      * @return
      *   a new iterable collection containing the found attributes
      */
    private def findAttributesByNames(names: Iterable[String]): Iterable[A] =
      names.flatMap(name => event.attributes.filter(_.name == name))
  extension [A <: Attribute, M <: Measure](
      events: Iterable[Event[A, M]]
  )
    /** Tests whether all these events have an attribute whose name is equal to
      * the specified name
      * @param name
      *   the attribute name to be matched
      * @return
      *   true if all these events have an attribute whose name matches the
      *   specified name; false otherwise
      */
    private def matchAttributeByName(name: String): Boolean =
      events.forall(e =>
        e.attributes.exists(
          _.name == name
        )
      )
