package folap.core

trait DimensionLevel:
  def parent: Option[DimensionLevel]
