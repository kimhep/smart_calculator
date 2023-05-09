package calculator

fun main() {
    val SAFE_WORD = "/exit"
    val HELP_WORD = "/help"
    val HELP_MESSAGE = "The program calculates the sum of numbers"
    val END_MESSAGE = "Bye!"

    loop@ while (true) {
        val input: List<String> = readLine()!!.split(" ").filter { it != null && !it.isNullOrBlank() }.map { it.trim() }
        var product = 0
        value@ for (value in input){
            when (value.lowercase()) {
                SAFE_WORD -> {
                    break@loop
                }
                HELP_WORD -> {
                    println(HELP_MESSAGE)
                    continue@loop
                }
                else -> {
                    val num = value.toIntOrNull() ?: continue@loop
                    product += num
                }
            }
        }
        println(product)
    }
    println(END_MESSAGE)
}
