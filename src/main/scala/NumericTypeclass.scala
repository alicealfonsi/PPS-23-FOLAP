trait TypeFromString[T]:
  def typeName: String
  def typology(name: String): Measure[T]

object TypeFromString:
  given TypeFromString[Int] with
    def typeName: String = "Int"
    def typology(name: String): Measure[Int] = Measure[Int](name, typeName)

  given TypeFromString[Double] with
    def typeName: String = "Double"
    def typology(name: String): Measure[Double] = Measure[Double](name, typeName)

  given TypeFromString[Long] with
    def typeName: String = "Long"
    def typology(name: String): Measure[Long] = Measure[Long](name, typeName)

  given TypeFromString[Float] with
    def typeName: String = "Float"
    def typology(name: String): Measure[Float] = Measure[Float](name, typeName)

  
  def resolve(name: String, typology: String): Measure[_] =
    val all = List(
      summon[TypeFromString[Int]],
      summon[TypeFromString[Double]],
      summon[TypeFromString[Long]],
      summon[TypeFromString[Float]]
    )

    all.find(_.typeName == typology) match
      case Some(tf) => tf.typology(name)
      case None     => throw new IllegalArgumentException(s"Type not supported: $typology")
