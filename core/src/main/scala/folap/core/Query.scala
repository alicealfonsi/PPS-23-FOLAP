package folap.core

final case class Query[A <: DimensionLevel](groupBySet: Set[A]):
  private def findParentAndChildren(attribute: A) =
    val parent = groupBySet.find(attribute.isChildrenOf(_))
    val children = groupBySet.find(_.isChildrenOf(attribute))
    (parent, children)

  def rollUp(attribute: A) =
    findParentAndChildren(attribute) match
      case (Some(_), _)        => this
      case (_, Some(children)) => Query(groupBySet - children + attribute)
      case (None, None)        => Query(groupBySet + attribute)

  def drillDown(attribute: A) =
    findParentAndChildren(attribute) match
      case (_, Some(children)) => this
      case (Some(parent), _)   => Query(groupBySet - parent + attribute)
      case (None, None)        => Query(groupBySet + attribute)

object Query {
  def create[A <: DimensionLevel](): Query[A] =
    Query(Set.empty)
}
