import java.beans.Expression

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

/*def weightChipsCur (potato: BigInt): Double => Double => Double = {
  (waterPotato: Double) => {
    (waterChips: Double) => {
      weightСhips(potato, waterPotato, waterChips)
    }
  }
}*/

weightChipsCur(90)
weightChipsCur(90)(0.9)
weightChipsCur(90)(0.9)(0.1)

// Task 2 A
def strToColDigits(str: String) : Int = {
  str.count(c => {c.isDigit})
}

def strToSumDigits(str: String) : Int = {
  var sum = 0
  for (i <- str.filter(c => {c.isDigit}))
    sum += i.toInt - 48
  sum
}

def strToColAlpha(str: String) : Int = {
  str.count(c => {c.isLetter})
}

strToSumDigits("12dsd34")
strToColDigits("12dsd34")
strToColAlpha("12dsd34")

// Task 2 B
def compareString(f: String => Int): (String, String) => Boolean = {
  (a, b) => {f(a) == f(b)}
}

// Task 2 C
println("Compare digits counts")
compareString(strToColDigits)("1234", "9876")
compareString(strToColDigits)("1234", "1234")
compareString(strToColDigits)("1234", "2e2e2e2")
compareString(strToColDigits)("aaa", "aaa")
compareString(strToColDigits)("11aaa11", "aaa")

println("Compare digits sums")
compareString(strToSumDigits)("1234", "9876")
compareString(strToSumDigits)("1234", "1234")
compareString(strToSumDigits)("1234", "2e2e2e2")
compareString(strToSumDigits)("aaa", "aaa")

println("Compare letters counts")
compareString(strToColAlpha)("1234", "9876")
compareString(strToColAlpha)("1234", "1234")
compareString(strToColAlpha)("1234", "2e2e2e2")
compareString(strToColAlpha)("aaa", "aaa")
compareString(strToColAlpha)("11aaa11", "a2a2a")

// Task 3
val getOpereationFunc: PartialFunction[String, (Double, Double) => Double] = {
  case "+" => (a:Double, b:Double) => a + b
  case "-" => (a:Double, b:Double) => a - b
  case "*" => (a:Double, b:Double) => a * b
  case "/" => (a:Double, b:Double) => a / b
  case _ => (_, _) => -1
}


def evaluate(input: String): Double = {
  val expr = input.split(" ")

  // Without extra function
  getOpereationFunc(expr(1))(expr(0).toDouble, expr(2).toDouble)
}

println(evaluate("1 + 2"))
println(evaluate("1 - 2"))
println(evaluate("1 / 2"))
println(evaluate("1.5 - 3"))
println(evaluate("5.2 / 2"))
println(evaluate("6 * 2.5"))
println(evaluate("7 / 0"))