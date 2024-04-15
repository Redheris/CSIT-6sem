def pack [T] (xs: List[T]): List[List[T]] = {
  var res: List[List[T]] = List.empty[List[T]]
  var last = xs
  while(last.nonEmpty) {
    val ys = last.takeWhile(_ == last.head)
    res = res.appended(ys)
    last = last.dropWhile(_ == ys.head)
  }
  res
}

def encode[T] (xs: List[T]): List[(T, Int)] = {
  pack(xs).map(lst => (lst.head, lst.length))
}

def encodeCounts[T] (xs: List[T]): List[(T, Int)] = {
  var res: List[(T, Int)] = List.empty
  var last = xs
  while (last.nonEmpty) {
    val ys = (last.head, last.count(_ == last.head))
    res = res.appended(ys)
    last = last.filter(_ != ys.head)
  }
  res
}

@main
def main(): Unit = {
  println(pack(List('a', 'a', 'a', 'b', 'b', 'a')))
  println(encode(List('a', 'a', 'a', 'b', 'b', 'a')))
  println(encodeCounts(List('a', 'a', 'a', 'b', 'b', 'a')))
}