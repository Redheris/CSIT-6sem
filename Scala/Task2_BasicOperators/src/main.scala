import scala.io.StdIn.readLine
import scala.util.Random
import scala.util.matching.Regex

def cond_oper(): Int = {
  val line = readLine("str: ")
  var result = -1
  try {
    val lineWords = line.split(' ')
    val a = lineWords(0).toInt
    val b = lineWords(2).toInt
    val op = lineWords(1)
    result = op match {
      case "+" => a + b
      case "-" => a - b
      case "*" => a * b
      case "/" => a / b
      case _ => throw new Exception
    }
//    if (op == "+") result = a + b
//    else if (op == "-") result = a - b
//    else if (op == "*") result = a * b
//    else if (op == "/") result = a / b
//    else throw new Exception
  }
  catch
    case e: Exception => println("Incorrect input")
  result
}

def cycles(): Unit = {
  var str: String = readLine("1. Enter a string: ")
  var sum: Int = 0
  for (c <- str)
    if (c.isDigit)
      sum += c.toInt - 48
  println(s"Sum of digits: $sum")
  str = readLine("2. Enter a string: ")
  var mulCodes: Long = 1
  for (c <- str)
    if (c.isLetter)
      mulCodes *= c.toInt
  println(s"Multiplication of letters' codes: $mulCodes")
  val n: Int = readLine("3. Enter a number: ").toInt
  for (i <- 1 to n) {
    for (j <- n to n - i + 1 by -1)
      print(s"$j ")
    println()
  }
  println("Try to guess a number from 1 to 100")
  val ans: Int = Random.between(1, 100)
  var guess: Int = 0
  while (guess != ans)
    guess = readLine("Your guess: ").toInt
    if (guess < ans)
      println("Too low")
    else if (guess > ans)
      println("Too high")
  println("Correct!")
}

def matching(): Unit = {
  var str: String = readLine("1. Enter a string: ")
  val regexAutoNumber: Regex = "[a-zA-Z][0-9]{3}[a-zA-Z]{2}".r
  println(regexAutoNumber.matches(str))
  str = readLine("2. Enter a password string: ")
  var flag = true
  if (str.length < 8)
    println("Password should be at least 8 characters long")
    flag = false
  else if (!str.matches(".*[a-z].*"))
    println("Password should contain at least one lowercase letter")
    flag = false
  else if (!str.matches(".*[A-Z].*"))
    println("Password should contain at least one uppercase letter")
    flag = false
  else if (!str.matches(".*[0-9].*"))
    println("Password should contain at least one digit")
    flag = false
  else if (!str.matches(".*\\W.*"))
    println("Password should contain at least one special character")
    flag = false
  println(flag)
  str = readLine("3. Enter a date string: ")
  val regexDate: Regex = "([0-9]?[0-9])\\.([0-9]?[0-9])\\.([0-9]{4})".r
  if (!regexDate.matches(str))
    println("Incorrect date format")
  else
    print("Correct date format")
}

@main
def main(): Unit = {
  val taskNum: Int = readLine("Enter task part's number (1-3): ").toInt
  taskNum match {
    case 1 => println(cond_oper())
    case 2 => cycles()
    case 3 => matching()
    case _ => println("Incorrect task number")
  }
}

