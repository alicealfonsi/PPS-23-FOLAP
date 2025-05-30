package folap.core

trait DimensionLevel:
  def parent: Option[DimensionLevel]
  def isChildrenOf(other: DimensionLevel): Boolean =
    parent match
      case None                  => false
      case Some(p) if p == other => true
      case Some(p)               => p.isChildrenOf(other)
