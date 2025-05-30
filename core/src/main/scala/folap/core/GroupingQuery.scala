package folap.core

final case class GroupingQuery[A <: DimensionLevel](groupBySet: Set[A]):
  private def findParentAndChildren(attribute: A) =
    val parent = groupBySet.find(attribute.isChildrenOf(_))
    val children = groupBySet.find(_.isChildrenOf(attribute))
    (parent, children)

  def rollUp(attribute: A) =
    findParentAndChildren(attribute) match
      case (Some(_), _) => this
      case (_, Some(children)) =>
        GroupingQuery(groupBySet - children + attribute)
      case (None, None) => GroupingQuery(groupBySet + attribute)

  def drillDown(attribute: A) =
    findParentAndChildren(attribute) match
      case (_, Some(children)) => this
      case (Some(parent), _)   => GroupingQuery(groupBySet - parent + attribute)
      case (None, None)        => GroupingQuery(groupBySet + attribute)

object GroupingQuery {
  def create[A <: DimensionLevel](): GroupingQuery[A] =
    GroupingQuery(Set.empty)
}
