class MatrixSquareBoolean(n: Int) {
  private var matrix: Vector[Vector[Boolean]] = Vector.fill(n)(Vector.fill(n)(false))

  def size: Int = n

  def this(matrix: Vector[Vector[Boolean]]) = {
    this(matrix.size)
    this.setMatrix(matrix)
  }

  def this(fromInt: Boolean, matrix: Vector[Vector[Int]]) = {
    this(matrix.size)
    this.setMatrixFromInt(matrix)
  }

  // Установка матрицы из вектора векторов
  private def setMatrix(matrix: Vector[Vector[Boolean]]): Unit = {
    this.matrix = matrix
  }

  private def setMatrixFromInt(matrix: Vector[Vector[Int]]): Unit = {
    this.matrix = matrix.map(_.map(_ != 0))
  }

  // Получение вектора векторов матрицы
  def getMatrix: Vector[Vector[Boolean]] = {
    matrix
  }

  // Получение (row, col)-го элемента
  def apply(row: Int, col: Int): Boolean = {
    matrix(row)(col)
  }

  // Обновление (row, col)-го элемента
  def update(row: Int, col: Int, value: Boolean): Unit = {
    matrix = matrix.updated(row, matrix(row).updated(col, value))
  }

  // Ввод матрицы из терминала
  def readMatrix(): Unit = {
    println("Введите матрицу построчно через пробелы.\nРазмер матрицы: " + n)
    for (i <- 0 until n)
      val row = scala.io.StdIn.readLine().split(" ").map(_.toInt)
      if (row.length != n)
        throw new IllegalArgumentException("Invalid input")
      for (j <- row.indices)
        this(i,j) = row(j) != 0
  }

  // Метод для вывода матрицы
  def display(showNumbers: Boolean = false): Unit = {
    if (showNumbers) {
      print("\t")
      for (i <- 0 until n)
        print(i + "\t")
      println()
    }
    for (i <- 0 until n)
      if (showNumbers) print (i + "\t")
      for (j <- 0 until n)
        print((if matrix(i)(j) then 1 else 0) + "\t")
      println()
  }
}