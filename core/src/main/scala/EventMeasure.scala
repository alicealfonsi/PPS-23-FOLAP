package folap.core

import MultidimensionalModel.Measure

/** An EventMeasure is a Measure specific to events related to the same fact
  */
trait EventMeasure[T] extends Measure[T]
