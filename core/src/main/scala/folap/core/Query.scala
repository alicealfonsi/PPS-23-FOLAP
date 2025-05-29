package folap.core

final case class Query[A](groupBySet: Set[A]):
  def rollUp(attribute: A) =
    Query(groupBySet + attribute)

object Query {
  def create[A](): Query[A] =
    Query(Set.empty)
}
