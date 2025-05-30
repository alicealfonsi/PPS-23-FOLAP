package folap.core

final case class Query[A <: DimensionLevel](groupBySet: Set[A]):
  def rollUp(attribute: A) =
    Query(groupBySet + attribute)

object Query {
  def create[A <: DimensionLevel](): Query[A] =
    Query(Set.empty)
}
