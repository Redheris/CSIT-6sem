// Результат отличается от 3 на 4e-16
var t1: Double = Math.pow(Math.sqrt(3), 2)
// csitcsitcsit
var t2: String = "csit" * 3
// Возвращает максимальный из операндов. Определён в классе RichInt
var t3 = 10 max 2

var t4: BigInt = 2
t4 pow 1024

import scala.util.Random
import BigInt.probablePrime
var t5: BigInt = probablePrime(100, Random)

var t6: String = "0task61"
s"${t6.take(1)} ${t6.takeRight(1)}"
s"${t6(0)} ${t6(t6.length - 1)}"

/**
 * Функции для работы с массивами и строками
 * drop(n) - возвращает без первых n элементов
 * take(n) - возвращает первые n элементов
 * dropRight(n) - симметричный аналог drop
 * takeRight(n) - симметричный аналог take
 */
var t7arr: Array[Int] = Array(1, 2, 3, 4, 5)
var t7str: String = "1,2,3,4,5"
t7arr.take(3)
t7str.drop(3)
t7arr.takeRight(3)
t7str.dropRight(3)