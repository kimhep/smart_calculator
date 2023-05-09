package calculator

fun main() {
    val input: List<Int> = readLine()!!.split(" ").map{it.toInt()}  // map{it.toInt()} map gets a lambda expression (here it.toInt())

    println(input[0] + input[1])
}