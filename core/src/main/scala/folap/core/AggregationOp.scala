package folap.core

/** Sum type consisting of the aggregation operators that can be applied to
  * combine the measures values of primary events
  */
enum AggregationOp:
  case Sum
  case Avg
  case Min
  case Max
