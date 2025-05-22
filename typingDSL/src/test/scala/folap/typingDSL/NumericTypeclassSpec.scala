package folap.typingDSL
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class TypeFromStringSpec extends AnyFlatSpec with Matchers:

  "TypeFromString" should "resolve Int Measure correctly" in:
    val m = TypeFromString.resolve("quantity", "Int")
    m shouldBe Some(Measure("quantity", "Int"))

  "TypeFromString[Int]" should "return typeName = Int" in:
    val tf = summon[TypeFromString[Int]]
    tf.typeName shouldBe "Int"

  "TypeFromString" should "resolve Double Measure correctly" in:
    val m = TypeFromString.resolve("price", "Double")
    m shouldBe Some(Measure("price", "Double"))

  "TypeFromString[Double]" should "return typeName = Double" in:
    val tf = summon[TypeFromString[Double]]
    tf.typeName shouldBe "Double"

  "TypeFromString" should "resolve Long Measure correctly" in:
    val m = TypeFromString.resolve("price", "Long")
    m shouldBe Some(Measure("price", "Long"))

  "TypeFromString[Long]" should "return typeName = Long" in:
    val m = TypeFromString.resolve("price", "Long")
    m shouldBe Some(Measure("price", "Long"))

  "TypeFromString" should "resolve Float Measure correctly" in:
    val m = TypeFromString.resolve("price", "Float")
    m shouldBe Some(Measure("price", "Float"))

  "TypeFromString[Float]" should "return typeName = Float" in:
    val m = TypeFromString.resolve("price", "Float")
    m shouldBe Some(Measure("price", "Float"))

  it should "return None for unsupported types" in:
    val m = TypeFromString.resolve("wright", "Decimal")
    m shouldBe None
