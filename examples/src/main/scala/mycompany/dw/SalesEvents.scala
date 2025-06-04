package mycompany.dw
import Sales.Dimension.TemporalDimension._
import Sales.Dimension.GeographicDimension._
import Sales.Dimension.ProductDimension._
import Sales.{Revenue, Quantity}

object SalesEvents:

  val year2023 = Year("2023")
  val year2024 = Year("2024")

  val quarterQ1_2023 = Quarter("Q1", year2023)
  val quarterQ2_2023 = Quarter("Q2", year2023)
  val quarterQ1_2024 = Quarter("Q1", year2024)

  val monthJanuary_2023 = Month("January", quarterQ1_2023)
  val monthJanuary_2024 = Month("January", quarterQ1_2024)
  val monthApril_2023 = Month("April", quarterQ2_2023)

  val regionNorthAmerica = Region("North America")
  val countryUSA = Country("USA", regionNorthAmerica)
  val cityNewYork = City("New York", countryUSA)
  val regionEurope = Region("Europe")
  val countryGermany = Country("Germany", regionEurope)
  val cityBerlin = City("Berlin", countryGermany)
  val countryItaly = Country("Italy", regionEurope)
  val cityMilan = City("Milan", countryItaly)

  val categoryElectronics = Category("Electronics")
  val subCategorySmartphones =
    SubCategory("Smartphones", categoryElectronics)
  val productIPhone = Product("iPhone 14", subCategorySmartphones)
  val categoryHomeAppliances =
    Category("Home Appliances")
  val subCategoryKitchen =
    SubCategory("Kitchen", categoryHomeAppliances)
  val productBlender = Product("Blender", subCategoryKitchen)
  val subCategoryLaptops =
    SubCategory("Laptops", categoryElectronics)
  val productMacbook = Product("MacBook Air", subCategoryLaptops)

  val valueQuantity = 10
  val quantitySold = Quantity(valueQuantity)
  val valueRevenue = 12000.0
  val revenueAmount = Revenue(valueRevenue)
  val salesEvent1 = Sales.Sales(
    revenueAmount,
    quantitySold,
    monthJanuary_2023,
    cityNewYork,
    productIPhone
  )

  val quantity2 = Quantity(5)
  val revenue2 = Revenue(250.0)
  val salesEvent2 = Sales.Sales(
    revenue2,
    quantity2,
    monthApril_2023,
    cityBerlin,
    productBlender
  )

  val quantity3 = Quantity(2)
  val revenue3 = Revenue(2400.0)
  val salesEvent3 = Sales.Sales(
    revenue3,
    quantity3,
    monthJanuary_2024,
    cityMilan,
    productMacbook
  )

  val quantity4 = Quantity(3)
  val revenue4 = Revenue(3600.0)
  val salesEvent4 = Sales.Sales(
    revenue4,
    quantity4,
    monthJanuary_2024,
    cityBerlin,
    productIPhone
  )
  val salesEvents = Iterable(salesEvent1, salesEvent2, salesEvent3, salesEvent4)
