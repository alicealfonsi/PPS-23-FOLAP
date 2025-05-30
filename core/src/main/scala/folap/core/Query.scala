package folap.core

final case class Query[A <: DimensionLevel](groupBySet: Set[A]):
  def rollUp(attribute: A) =
    val parent = groupBySet.find(attribute.isChildrenOf(_))
    if parent.isDefined then this
    else Query(groupBySet + attribute)

object Query {
  def create[A <: DimensionLevel](): Query[A] =
    Query(Set.empty)
}
