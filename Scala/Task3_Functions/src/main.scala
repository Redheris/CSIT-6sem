import scala.io.StdIn.readLine
import scala.math.Numeric.BigDecimalIsFractional.abs

def sqrt(x: Double): BigDecimal = {
  def sqrtIter(guess: BigDecimal): BigDecimal =
    if isGoodEnough(guess) then guess
    else sqrtIter(improve(guess))
  def improve(guess: BigDecimal): BigDecimal =
    (guess + x / guess) / 2
  def isGoodEnough(guess: BigDecimal) =
    abs(guess * guess - x) < 1e-50
  sqrtIter(1)
}

def factorial (n: Int) : Int = {
  if (n <= 1) 1;
  else n * factorial(n - 1);
}

def pascal (c: Int, r: Int): Int = {
  factorial(r) / (factorial(c) * factorial(r - c))
}

def balance(chars: List[Char]): Boolean = {
  def balanceIter(i: Int, opens: Int) : Boolean = {
    if (i >= chars.length) true
    else if (chars(i).equals('('))
      balanceIter(i + 1, opens + 1)
    else if (chars(i).equals(')'))
      if (opens == 0) false
      else balanceIter(i + 1, opens - 1)
    else balanceIter(i + 1, opens)
  }
  balanceIter(0, 0)
}

def countChange(money: Int, coins: List[Int]): Int = {
  def counterIter(i: Int, amount: Int): Int = {
    if (i == coins.length || amount < 0) return 0
    if (amount == 0) return 1
    counterIter(i + 1, amount) + counterIter(i, amount - coins(i))
  }
  counterIter(0, money)
}

@main
def main(): Unit = {
  val task_num: Int = readLine("Enter a task number: ").toInt
  task_num match {
    case 1 => println(sqrt(readLine("Sqrt. Enter x = ").toDouble));
    case 2 =>
      println("Pascal triangle");
      val col = readLine("Enter column = ").toInt;
      val row = readLine("Enter row = ").toInt;
      println(pascal(col, row));
    case 3 => println(balance(readLine("Balance. Enter a string = ").toList));
    case 4 =>
      println("Count changes")
      val money: Int = readLine("Enter a money number = ").toInt
      val coins: List[Int] = readLine("Enter a list of nominals = ").split(" ").map(i => i.toInt).toList
      println(countChange(money, coins))
  }
}