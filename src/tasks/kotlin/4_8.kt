package calculator

import java.lang.NumberFormatException

fun getFactor(signString: String): String {
    return if (signString.filter { it == '-' }.length % 2 == 0) {"+"} else "-"
}

/* fun main() {
    val SAFE_WORD = "/exit"
    val HELP_WORD = "/help"
    val HELP_MESSAGE = "The program calculates the sum of numbers. It can also handle + and -."
    val END_MESSAGE = "Bye!"

    loop@ while (true) {
        val input: List<String> = readln().split(" ").filter { it != null && !it.isNullOrBlank() }.map { it.trim() }
        if (input.isEmpty()) {continue@loop}
        var product = 0
        var i = 0
        while (i <= input.size -1) {
            when (input[i].lowercase()) {
                SAFE_WORD -> {
                    break@loop
                }

                HELP_WORD -> {
                    println(HELP_MESSAGE)
                    continue@loop
                }
                else -> {
                    var (summand: Int, ind: Int) = try {
                        Pair(input[i].toInt(), i)
                    } catch (e: NumberFormatException) {
                        Pair(input[i + 1].toInt() * getFactor(input[i]), i + 1)
                    }
                    i = ind +1
                    product += summand
                }
            }
        }
        println(product)
    }
    println(END_MESSAGE)
}


/*
package calculator

fun main() {
    while (true) {
        val action = readln()
        when {
            action.isEmpty() -> continue
            action == "/exit" -> break
            else -> println(action.split(' ').sumOf { it.toInt() })
        }
    }
    println("Bye!")
}
*/