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

def task_2(): Unit = {
  println("====================== Task-2 ======================")
  println
}
def findOrderStatistic(array: Array[Int], k: Int): Int = {

  def iter(lst: List[Int], mid: Int, midVal: Int, ): Int = {

    k match {
      case mid => k
      case k if k < mid =>
    }
  }

  iter (lst.tail, 1, lst.head)
}

  def orderStatistic[T](lst: List[T], k: Int)(lesser: (T, T) => Boolean): T = {
    def orderStatisticHelper(lst: List[T], mid: Int, midVal: T, begins: Int, left: List[T], right: List[T]): T = {
      if (lst.isEmpty)
        if (k == mid)
          midVal
        else if (k < mid)
          orderStatisticHelper(left.tail, begins, left.head, begins, List[T](), List[T]())
        else
          orderStatisticHelper(right.tail, mid + 1, right.head, mid + 1, List[T](), List[T]())
      else if (lesser(lst.head, midVal))
        orderStatisticHelper(lst.tail, mid + 1, midVal, begins, lst.head :: left, right)
      else
        orderStatisticHelper(lst.tail, mid, midVal, begins, left, lst.head :: right)
    }

    orderStatisticHelper(lst.tail, 1, lst.head, 1, List[T](), List[T]())
  }

  var left = 0
  var right = array.length

  while (true) {
    val mid = partition(array, left, right)
    if (mid == k) return array(mid)
    else if (k < mid) right = mid
    else left = mid + 1
  }
}

def quickSelect[T](list: List[T], k: Int)(implicit ord: Ordering[T]): T = {
  if (list.isEmpty) throw new NoSuchElementException("List is empty")
  if (k < 1 || k > list.length) throw new IllegalArgumentException("Invalid k")

  val pivotIndex = Random.nextInt(list.length)
  val (pivot, rest) = list.splitAt(pivotIndex)
  val (less, greater) = rest.partition(_ < pivot.head)

  if (k <= less.length) quickSelect(less, k)
  else if (k > less.length + 1) quickSelect(greater, k - less.length - 1)
  else pivot.head
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
