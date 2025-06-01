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

  def drillAcross[
      L1,
      A1 <: Attribute[L1],
      M1 <: Measure,
      L2,
      A2 <: Attribute[L2],
      M2 <: Measure
  ](
      events: Iterable[Event[L1, A1, M1]],
      otherEvents: Iterable[Event[L2, A2, M2]],
      createEvent: EventConstructor[L1 | L2, Attribute[L1 | L2], M1 | M2]
  ): Iterable[Event[L1 | L2, Attribute[L1 | L2], M1 | M2]] = {

    def leafAttributes(attrs: Iterable[A1 | A2]): Iterable[Attribute[L1 | L2]] =
      val allParents = attrs.flatMap(_.parent)
      // attrs.filterNot(a => allParents.exists(_ == a))
      ???

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

  def sliceAndDice[L, A <: Attribute[L], M <: Measure](
      events: Iterable[Event[L, A, M]],
      filters: Iterable[(L, String)]
  ): Iterable[Event[L, A, M]] =
    events.filter { event =>
      filters.forall { filter =>
        event.attributes
          .find(attr => attr == filter._1)
          .exists(_.value == filter._2)
      }

    }

  import Cube.*
  import Additivity.*, AggregationOperator.*
  def rollUp[L, A <: Attribute[L], M <: Measure, E <: Event[L, A, M]](
      events: Iterable[E]
  )(groupBySet: Iterable[L])(aggregationOperator: AggregationOperator)(using
      operational: Operational[L, A, M, E]
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
          case Max => events.aggregateByMaximum(groupBySet)
      )
    else events
