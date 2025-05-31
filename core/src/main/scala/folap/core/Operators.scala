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
      createEvent: EventConstructor[A1, M1 | M2]
  ): Iterable[Event[A1, M1 | M2]] = {
    events.flatMap { eventA =>
      otherEvents.flatMap { eventB =>
        val commonAttributes = eventA.attributes.filter { attrA =>
          eventB.attributes.exists(attrB =>
            attrB.name == attrA.name && attrB.value == attrA.value
          )
        }

        if (commonAttributes.nonEmpty) {
          val combinedMeasures: Iterable[M1 | M2] =
            eventA.measures ++ eventB.measures
          Some(createEvent(commonAttributes, combinedMeasures))
        } else {
          None
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

  import Cube.*
  import Additivity.*, AggregationOperator.*
  def rollUp[A <: Attribute, M <: Measure, E <: Event[A, M]](
      events: Iterable[E]
  )(groupBySet: Iterable[String])(aggregationOperator: AggregationOperator)(
      using operational: Operational[A, M, E]
  ): Iterable[E] =
    if groupBySet.exists(name => events.matchAttributeByName(name)) then
      val groupByMap =
        events.groupBy(
          _.findAttributesByNames(groupBySet).map(_.value)
        )
      groupByMap.values.map(events =>
        aggregationOperator match
          case Sum => events.aggregateBySum(groupBySet)
          case Avg => events.aggregateByAverage(groupBySet)
          case Min => events.aggregateByMinimum(groupBySet)
          case Max => ???
      )
    else events
