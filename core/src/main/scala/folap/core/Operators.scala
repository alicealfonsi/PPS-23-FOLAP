package folap.core

import multidimensionalmodel.{Attribute, Measure}

/** Operators for querying and manipulating events of a multidimensional data
  * warehouse
  */

object Operators:
  /** Slice and dice operation used to filter events by matching attribute names
    * and values.
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

  /** Returns only the leaf attributes from a collection of attributes.
    *
    * A leaf attribute is defined as an attribute that does not appear as a
    * parent of any other attribute in the given collection.
    *
    * @param attrs
    *   the collection of attributes to process
    * @tparam A
    *   the type of attributes
    * @return
    *   a collection of attributes that are not parents of any other attributes
    */
  private def leafAttributes[A <: Attribute](attrs: Iterable[A]): Iterable[A] =
    val allParents = attrs.flatMap(_.parent)
    attrs.filterNot(a => allParents.exists(_ == a))

  /** Compares two attributes and returns true if they share the exact same
    * hierarchy path.
    *
    * Two attributes are considered to have the same hierarchy if:
    *   - They have the same name and value
    *   - Their respective parents, grandparents, etc. match exactly in both
    *     name and value
    *
    * @param a1
    *   the first attribute
    * @param a2
    *   the second attribute
    * @tparam A
    *   the attribute type, which must extend `Attribute`
    * @return
    *   true if the two attributes share the same hierarchical structure and
    *   values
    */
  private def sameHierarchy[A <: Attribute](a1: A, a2: A): Boolean =
    def asPath(attr: A): List[(String, String)] =
      attr.hierarchy.map(a => (a.name, a.value)).toList

    asPath(a1) == asPath(a2)

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
      A <: Attribute,
      A1 <: A,
      A2 <: A,
      M <: Measure,
      M1 <: Measure,
      E1 <: Event[A1, M],
      E2 <: Event[A2, M1],
      E <: Event[A1, M | M1]
  ](
      events: Iterable[E1],
      otherEvents: Iterable[E2],
      createEvent: EventConstructor[A1, M | M1, E]
  ): Iterable[E] = {

    events.flatMap { eventA =>
      val leavesA = leafAttributes(eventA.attributes)

      otherEvents.flatMap { eventB =>
        val leavesB = leafAttributes(eventB.attributes)

        val commonLeaves = leavesA.filter { attrA =>
          leavesB.exists(attrB => sameHierarchy(attrA, attrB))
        }

        if (commonLeaves.nonEmpty) {
          val combinedMeasures: Iterable[M | M1] =
            eventA.measures ++ eventB.measures
          Seq(createEvent(commonLeaves, combinedMeasures))
        } else {
          Nil
        }
      }
    }
  }

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
