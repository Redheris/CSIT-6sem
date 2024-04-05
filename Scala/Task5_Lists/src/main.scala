def sort(lst: List[Int]): List[Int] = {
  def sortIter(sorted: List[Int], sublst: List[Int]) : List[Int] = {
    sublst match
      case x :: Nil => sorted
      case hd :: tl =>
        if (hd <= tl.head)
        else tl.appended(hd)
  }
  sortIter(List.empty, lst.take(2))
}



def lazyList(a: Int = 1): LazyList[Int] =
  a #:: lazyList(a + 1)
val lst: List[Int] = 1 :: 2 :: 3 :: Nil
@main
def main(): Unit = {

  println(lst.drop(1))

  val r = "1 2 3 4".split(' ').toList.map(e => e.toInt)
}