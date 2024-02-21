import scala.io.StdIn.readLine

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

@main
def main(): Unit = {
  var taskNum: Int = readLine("Enter task part's number: ").toInt
  println(cond_oper())
}

