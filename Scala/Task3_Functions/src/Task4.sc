// Task 1 A
def weightСhips(potato: BigInt, waterPotato: Double, waterChips: Double): Double = {
  val potatoPart = potato.toDouble - waterPotato * potato.toDouble
  val weightWater = potato.toDouble * waterChips

  potatoPart * potato.toDouble / (potato.toDouble - weightWater)
}

BigDecimal(weightСhips(90, 0.9, 0.1)).setScale(2, BigDecimal.RoundingMode.HALF_UP)
BigDecimal(weightСhips(100, 0.85, 0.1)).setScale(2, BigDecimal.RoundingMode.HALF_UP)
BigDecimal(weightСhips(1000000000, 0.9, 0.05)).setScale(2, BigDecimal.RoundingMode.HALF_UP)

// Task 1 B
def weightChipsCurried = weightСhips.curried

weightChipsCurried
weightChipsCurried(90)
weightChipsCurried(90)(0.9)
weightChipsCurried(90)(0.9)(0.1)

// Task 1 C
def weightChipsCur (potato: BigInt)(waterPotato: Double)(waterChips: Double): Double = {
  weightСhips(potato, waterPotato, waterChips)
}

weightChipsCur(90)
weightChipsCur(90)(0.9)
weightChipsCur(90)(0.9)(0.1)

