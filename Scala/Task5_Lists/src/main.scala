import scala.annotation.tailrec
import scala.collection.*
import scala.io.StdIn.readLine

def insertSort[T](lst: List[T], f: (T, T) => Boolean): List[T] = {
  def sortIter(lst: List[T]) : List[T] = {
    lst match {
      case _ :: List() => lst
      case hd :: tlh :: tlt =>
        if (f(hd, tlh)) lst
        else tlh :: sortIter(hd :: tlt)
    }
  }
  lst match {
    case Nil => lst
    case _ :: List() => lst
    case hd :: tl => sortIter(hd :: insertSort(tl, f))
  }
}

def task_1(): Unit = {
  val lst_1: List[Int] = List(5, 4, 6, 9)
  println(s"Initial List: $lst_1")
  println(s"Sorted List: ${insertSort(lst_1, _ <= _)}")
  println

  val lst_2: List[Double] = List(1.3, 1.0, 9.4, 3.3)
  println(s"Initial List: $lst_2")
  println(s"Sorted List: ${insertSort(lst_2, _ <= _)}")
  println

  val lst_3: List[String] = List("aaa", "aba", "abc", "aab")
  println(s"Initial List: $lst_3")
  println(s"Sorted List: ${insertSort(lst_3, _ <= _)}")
  println
}

def findOrderStatistic[T](lst: List[T], k: Int, f: (T, T) => Boolean = (x: Int, y: Int) => {x < y}): T = {
  if (lst.isEmpty) throw new IllegalArgumentException("List is empty")
  if (k < 1 || k > lst.length) throw new IllegalArgumentException("Invalid k")

  val pivot = lst.head
  var less = List.empty[T]
  var greater = List.empty[T]

  for (x <- lst.tail) {
    if (f(x, pivot)) less = x :: less
    else greater = x :: greater
  }

  if (k <= less.length) findOrderStatistic(less, k, f)
  else if (k > less.length + 1) findOrderStatistic(greater, k - less.length - 1, f)
  else pivot
}

def task_2(): Unit = {
  println(s"4th order statistic in [9,2,5,3,10,1] (${List(9,2,5,3,10,1).sorted}):" +
    s" ${findOrderStatistic(List(9,2,5,3,10,1), 4)}")
  println(s"1st order statistic in [9,2,5,3,10,1] (${List(9,2,5,3,10,1).sorted}):" +
    s" ${findOrderStatistic(List(9,2,5,3,10,1), 1)}")
  println(s"6th order statistic in [10,25,2,64,16,28,5] (${List(10, 25, 2, 64, 16, 28, 5).sorted}):" +
    s"  ${findOrderStatistic(List(10, 25, 2, 64, 16, 28, 5), 6)}")
  println(s"4th order statistic in [9.1,2.7,2,3,10,10.1] (${List(9.1,2.7,2,3,10,10.1).sorted}):" +
    s" ${findOrderStatistic(List(9.1,2.7,2,3,10,10.1), 4, _ < _)}")
  println(s"4th order statistic in [\"aaa\", \"aba\", \"abc\", \"aab\"] (${List("aaa", "aba", "abc", "aab").sorted}):" +
    s" ${findOrderStatistic(List("aaa", "aba", "abc", "aab"), 4, _ < _)}")
  println

}

def primeFrom(a: Int): LazyList[Int] = {
  def nextPrime(x: Int, buf: LazyList[Int]): LazyList[Int] = {
    val lst: LazyList[Int] = buf.filter(_ % x != 0)
    if (x >= a)
      x #:: nextPrime(lst.head, lst)
    else
      nextPrime(lst.head, lst)
  }

  nextPrime(2, LazyList.from(2))
}

def task_3(): Unit = {
  println(primeFrom(1).takeWhile(_ <= 100).force)
  println(primeFrom(10).takeWhile(_ <= 100).force)
  println
}

type Point = (Int, Int)
type Ship = List[Point]
type Field = Vector[Vector[Boolean]]

def isEmpty(field: Field, ship: Ship): Boolean = {
  !ship.map(e => field(e._1)(e._2)).reduce(_ || _)
}

def placeShip(field: Field, ship: Ship): Field = {
  @tailrec
  def placeShipIter(field: Field, ship: Ship): Field = {
    ship match
      case Nil => field
      case x :: xs => placeShipIter(field.updated(x._1, field(x._1).updated(x._2, true)), xs)
  }
  if (isEmpty(field, ship))
    placeShipIter(field, ship)
  else
    println("Can't place ship here")
    field
}

private def printField(field: Field): Unit = {
  for (i <- field)
    println(i)
}

private def task_4(): Unit = {
  var field: Field = Vector.fill(10)(Vector.fill(10)(false))

  val ship1: Ship = List(
    (0, 0), (0, 1), (0, 2)
  )

  val ship2: Ship = List(
    (1, 1),
    (2, 1),
    (3, 1)
  )

  val ship3: Ship = List(
    (1, 0),
    (1, 1),
    (1, 2)
  )

  println("Placed ship 1")
  field = placeShip(field, ship1)
  printField(field)
  println

  println("Placed ship 2")
  field = placeShip(field, ship2)
  printField(field)
  println

  println("Failed to place ship 3")
  field = placeShip(field, ship3)
  printField(field)
  println
}

@main
def main(): Unit = {
  println("====================== Task 1 ======================")
  task_1()
  println("====================== Task 2 ======================")
  task_2()
  println("====================== Task 3 ======================")
  task_3()
  println("====================== Task 4 ======================")
  task_4()
}
