@main
def main(): Unit = {
  val ro_matrix =
    Vector(
      Vector(1, 0, 1, 0),
      Vector(0, 1, 0, 0),
      Vector(0, 0, 1, 0),
      Vector(0, 0, 0, 1)
    )
  val g_matrix =
    Vector(
      Vector(0, 1, 0, 0),
      Vector(0, 0, 0, 1),
      Vector(0, 1, 0, 1),
      Vector(1, 0, 0, 0),
    )
  val ro = new BinaryRelation(new MatrixSquareBoolean(true, ro_matrix))
  val g = new BinaryRelation(new MatrixSquareBoolean(true, g_matrix))
  ro.display("ro:")
  ro.displayInfo("ro")
  g.display("g:")
  g.displayInfo("g")

  ro.reverse().display("ro_reverse:")
  (ro + g).display("ro + g:")
  (ro & g).display("ro & g:")
  (ro \ g).display("ro \\ g:")
  (ro o g).display("ro o g:")
}