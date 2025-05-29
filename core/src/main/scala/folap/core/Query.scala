package folap.core

final case class Query[A](groupBySet: Set[A])

object Query {
  def create[A](): Query[A] =
    Query(Set.empty)
}
