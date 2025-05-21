package folap.typingDSL
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class TypeFromStringSpec extends AnyFlatSpec with Matchers:

  "TypeFromString[Int]" should "resolve Measure[Int] correctly" in:
    val m = TypeFromString.resolve("quantity", "Int")
    m shouldBe Some(Measure[Int]("quantity", "Int"))

  "TypeFromString[Int]" should "return typeName = Int" in:
    val tf = summon[TypeFromString[Int]]
    tf.typeName shouldBe "Int"

  "TypeFromString[Double]" should "resolve Measure[Double] correctly" in:
    val m = TypeFromString.resolve("price", "Double")
    m shouldBe Some(Measure[Double]("price", "Double"))

  "TypeFromString[Double]" should "return typeName = Double" in:
    val tf = summon[TypeFromString[Double]]
    tf.typeName shouldBe "Double"

  "TypeFromString[Long]" should "resolve Measure[Long] correctly" in:
    val m = TypeFromString.resolve("price", "Long")
    m shouldBe Some(Measure[Long]("price", "Long"))

  "TypeFromString[Long]" should "return typeName = Long" in:
    val m = TypeFromString.resolve("price", "Long")
    m shouldBe Some(Measure[Long]("price", "Long"))

  "TypeFromString[Float]" should "resolve Measure[Float] correctly" in:
    val m = TypeFromString.resolve("price", "Float")
    m shouldBe Some(Measure[Float]("price", "Float"))

  "TypeFromString[Float]" should "return typeName = Float" in:
    val m = TypeFromString.resolve("price", "Float")
    m shouldBe Some(Measure[Float]("price", "Float"))

  it should "return None for unsupported types" in:
    val m = TypeFromString.resolve("wright", "Decimal")
    m shouldBe None
