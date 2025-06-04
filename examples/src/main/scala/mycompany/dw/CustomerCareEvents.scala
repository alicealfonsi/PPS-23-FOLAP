package mycompany.dw

import CustomerCare.Dimension.GeographicDimension._
import CustomerCare.SatisfactionScore

object CustomerCareEvents:
  val regionEurope = Region("Europe")
  val countryGermany = Country("Germany", regionEurope)
  val cityBerlin = City("Berlin", countryGermany)
  val countryItaly = Country("Italy", regionEurope)
  val cityMilan = City("Milan", countryItaly)

  val customerCareEvent1 = CustomerCare.CustomerCare(
    SatisfactionScore(4.5),
    cityBerlin
  )

  val customerCareEvent2 = CustomerCare.CustomerCare(
    SatisfactionScore(3.8),
    cityMilan
  )
  val customerCareEvents = Iterable(customerCareEvent1, customerCareEvent2)
