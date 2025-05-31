package folap.core

import MultidimensionalModel._

/** Operators for querying and manipulating events of a multidimensional data
  * warehouse
  */

object Operators:
  /** Performs a drill across operation that combines events from two cubes when
    * they share at least one common leaf attribute.
    *
    * The resulting events will include:
    *   - The shared leaf attributes
    *   - All measures from matching events
    *
    * @param events
    *   the first collection of events
    * @param otherEvents
    *   the second collection of events to be combined
    * @param createEvent
    *   a constructor function used to build the resulting events from
    *   attributes and measures
    * @tparam A1
    *   attribute type of the first set of events
    * @tparam M1
    *   measure type of the first set of events
    * @tparam A2
    *   attribute type of the second set of events
    * @tparam M2
    *   measure type of the second set of events
    * @return
    *   a collection of events, each one resulting from the combination of two
    *   matching events sharing one or more common leaf attributes
    */
  private def leafAttributes[A <: Attribute](attrs: Iterable[A]): Iterable[A] =
      val allParents = attrs.flatMap(_.parent)
      attrs.filterNot(a => allParents.exists(_ == a))

  def drillAcross[
      A <: Attribute,
      A1 <: A,
      A2 <: A,
      M <: Measure,
      M2 <: Measure, 
      E1 <: Event[A1, M],
      E2 <: Event[A2, M2],
      E <: Event[A1, M | M2]
  ](
      events: Iterable[E1],
      otherEvents: Iterable[E2],
      createEvent: EventConstructor[A1, M | M2, E]
  ): Iterable[E] = {   

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
          val combinedMeasures: Iterable[M | M2] =
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

  def sliceAndDice[A <: Attribute, M <: Measure, E <: Event[A, M]](
      events: Iterable[E],
      filters: Iterable[Attribute]
  ): Iterable[E] =
    events.filter { event =>
      filters.forall { filter =>
        event.attributes
          .find(attr => attr.name == filter.name)
          .exists(_.value == filter.value)
      }

    }

  import Cube.*
  import AggregationOp.*
  def rollUp[A <: Attribute, M <: Measure, E <: Event[A, M]](
      events: Iterable[E]
  )(groupBySet: Iterable[String])(aggregationOp: AggregationOp)(
      using operational: Operational[A, M, E]
  ): Iterable[E] =
    if groupBySet.exists(name => events.matchAttributeByName(name)) then
      val groupByMap =
        events.groupBy(
          _.findAttributesByNames(groupBySet).map(_.value)
        )
      groupByMap.values.map(events =>
        aggregationOp match
          case Sum => events.aggregateBySum(groupBySet)
          case Avg => events.aggregateByAverage(groupBySet)
          case Min => events.aggregateByMinimum(groupBySet)
          case Max => events.aggregateByMaximum(groupBySet)
      )
    else events
