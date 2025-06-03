package folap.core
/** Represents the types of aggregation that can be applied to measures
*/
enum AggregationOp:
   /** Aggregation by summing all values
   */
  case Sum
  /** Aggregation by computing the arithmetic mean of the values
  */
  case Avg
   /** Aggregation by selecting the minimum value
   */
  case Min
  /** Aggregation by selecting the maximum value
  */
  case Max
