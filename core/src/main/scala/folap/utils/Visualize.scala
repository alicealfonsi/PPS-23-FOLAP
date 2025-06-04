package folap.utils
import folap.core.Event
import folap.core.multidimensionalModel._
/**
 * Displays a sequence of events in a readable, structured format.
 *
 * @param events 
 * a collection of events.
 */
def visualize(events: Iterable[Event[_, _]]): Unit =
  events.zipWithIndex.foreach { (event, idx) =>
    println(s"--- Event ${idx + 1} ---")

    val expanded = event.attributes
      .flatMap(_.hierarchy)
      .filter(_.name != "TopAttribute")

    val seen = scala.collection.mutable.Set[String]()
    val orderedDims = expanded.filter { a =>
      val key = a.name + ":" + a.value
      if seen.contains(key) then false else { seen += key; true }
    }

    orderedDims.foreach { attr =>
      println(s"${attr.name}: ${attr.value}")
    }

    event.measures.foreach { m =>
      println(s"${m.name}: ${m.value}")
    }

    println()
  }
