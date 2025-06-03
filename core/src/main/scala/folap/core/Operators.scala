package folap.core

import multidimensionalModel._

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

  import Cube.*
  import AggregationOp.*

  /** Performs events aggregation based on the specified group-by set and
    * aggregation operator
    * @param events
    *   primary events to aggregate
    * @param groupBySet
    *   the names of the attributes against which to aggregate
    * @param aggregationOperator
    *   the operator according to which aggregate the measures values of primary
    *   events
    * @param computable
    *   a Computable type class instance
    * @tparam A
    *   the type of Event attributes, which must be a subtype of Attribute
    * @tparam M
    *   the type of Event measures, which must be a subtype of Measure
    * @tparam E
    *   the events type, which must be a subtype of Event[A, M]
    * @return
    *   secondary events resulting from the aggregation
    */
  def rollUp[A <: Attribute, M <: Measure, E <: Event[A, M]](
      events: Iterable[E]
  )(groupBySet: Iterable[String])(aggregationOperator: AggregationOp)(using
      computable: Computable[A, M, E]
  ): Iterable[E] =
    if groupBySet.exists(name => events.matchAttributeByName(name)) then
      val groupByMap =
        events.groupBy(
          _.findAttributesByNames(groupBySet).map(_.value)
        )
      groupByMap.values.map(events =>
        aggregationOperator match
          case Sum => events.aggregateBy(Sum)(groupBySet)
          case Avg => events.aggregateBy(Avg)(groupBySet)
          case Min => events.aggregateBy(Min)(groupBySet)
          case Max => events.aggregateBy(Max)(groupBySet)
      )
    else events
