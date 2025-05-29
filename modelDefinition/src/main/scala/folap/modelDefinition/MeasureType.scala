package folap.modelDefinition

/** A union type representing the allowed numeric types for a Measure.
  *
  * *Any attempt to use a different type (e.g., String, Boolean, etc.) will
  * result in a compile-time error.
  */
type MeasureType = Int.type | Long.type | Float.type | Double.type
