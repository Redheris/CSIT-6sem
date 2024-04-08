class BinaryRelation(matrix: MatrixSquareBoolean) {
  private val relationMatrix: MatrixSquareBoolean = matrix

  def apply(row: Int, col: Int): Boolean = {
    relationMatrix(row, col)
  }

  def reverse(): BinaryRelation = {
    val matrixClone = MatrixSquareBoolean(relationMatrix.getMatrix)
    for (i <- 0 until relationMatrix.size)
      for (j <- 0 until relationMatrix.size)
        matrixClone(i, j) = matrix(j, i)
    new BinaryRelation(matrixClone)
  }

  def +(other: BinaryRelation): BinaryRelation = {
    val matrixClone = MatrixSquareBoolean(relationMatrix.getMatrix)
    for (i <- 0 until relationMatrix.size)
      for (j <- 0 until relationMatrix.size)
        matrixClone(i, j) = relationMatrix(i, j) | other(i, j)
    new BinaryRelation(matrixClone)
  }

  def &(other: BinaryRelation): BinaryRelation = {
    val matrixClone = MatrixSquareBoolean(relationMatrix.getMatrix)
    for (i <- 0 until relationMatrix.size)
      for (j <- 0 until relationMatrix.size)
        matrixClone(i, j) = relationMatrix(i, j) & other(i, j)
    new BinaryRelation(matrixClone)
  }

  def \(other: BinaryRelation): BinaryRelation = {
    val matrixClone = MatrixSquareBoolean(relationMatrix.getMatrix)
    for (i <- 0 until relationMatrix.size)
      for (j <- 0 until relationMatrix.size)
        matrixClone(i, j) = relationMatrix(i, j) & !other(i, j)
    new BinaryRelation(matrixClone)
  }

  def o(other: BinaryRelation): BinaryRelation = {

    val matrixClone = MatrixSquareBoolean(relationMatrix.size)
    for (i <- 0 until relationMatrix.size)
      for (j <- 0 until relationMatrix.size)
        for (k <- 0 until relationMatrix.size)
          if (!matrixClone(i, j))
            matrixClone(i, j) = relationMatrix(i, k) & other(k, j)
    new BinaryRelation(matrixClone)
  }

  def isReflexive: Boolean = {
    for (i <- 0 until relationMatrix.size)
      if (!relationMatrix(i, i))
        return false
    true
  }

  def isSymmetric: Boolean = {
    for (i <- 0 until relationMatrix.size)
      for (j <- 0 until relationMatrix.size)
        if (relationMatrix(i, j) != relationMatrix(j, i))
          return false
    true
  }

  def isAntisymmetric: Boolean = {
    for (i <- 0 until relationMatrix.size)
      for (j <- 0 until relationMatrix.size)
        if (i != j && relationMatrix(i, j) && relationMatrix(j, i))
          return false
    true
  }

  def isTransitive: Boolean = {
    for (i <- 0 until relationMatrix.size)
      for (j <- 0 until relationMatrix.size)
        for (k <- 0 until relationMatrix.size)
          if (relationMatrix(i, j) && relationMatrix(j, k) && !relationMatrix(i, k))
            return false
    true
  }

  def isEquivalence: Boolean = {
    isReflexive && isSymmetric && isTransitive
  }

  def closureReflexive: BinaryRelation = {
    val matrixClone = MatrixSquareBoolean(relationMatrix.getMatrix)
    for (i <- 0 until relationMatrix.size)
      matrixClone(i, i) = true
    new BinaryRelation(matrixClone)
  }

  def closureSymmetric: BinaryRelation = {
    this + this.reverse()
  }

  def closureTransitive: BinaryRelation = {
    var prev = this
    var result = (this o this) + this

    while (prev.relationMatrix.getMatrix != result.relationMatrix.getMatrix) {
      prev = result
      result = (result o this) + result
    }
    result
  }

  def closureEquivalence: BinaryRelation = {
    this.closureSymmetric.closureReflexive.closureTransitive
  }

  def displayInfo(name: String = "relation"): Unit = {
    println(s"Characteristics of binary relation: $name")
    println(s"Is $name reflexive: $isReflexive")
    println(s"Is $name symmetric: $isSymmetric")
    println(s"Is $name antisymmetric: $isAntisymmetric")
    println(s"Is $name transitive: $isTransitive")
    println(s"Is $name equivalence: $isEquivalence")
    println()
  }
  def display(str: String = ""): Unit = {
    if (str != "")
      println(str)
    relationMatrix.display()
    println()
  }
}