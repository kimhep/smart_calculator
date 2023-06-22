package calculator

import java.lang.NumberFormatException

 /* fun checkOccurrences(string: String): Boolean {
    val plusMinusCount = Regex("[+-]").findAll(string).count()
    val digitCount = string.replace("+", " ").replace("-", " ").split(" ").size
    return plusMinusCount == digitCount - 1
}

fun parseInput(input: String): String {
    var positive = true
    var lastCharInt = false
    var tempIntStr = ""
    for (char in input) {
        if (char.digitToIntOrNull() != null) {
            if (lastCharInt) {
                tempIntStr += char
            }
            else {
                tempIntStr += if (positive) " $char" else (" -$char")
                positive = true
            }
            lastCharInt = true
        }
        else if (char == '-') {
            lastCharInt = false
            positive = !positive
        }
        else if (char == '+') {
            lastCharInt = false
            positive = true
        }
    }
    return tempIntStr
} */
/*
fun main() {
    val SAFE_WORD = "/exit"
    val HELP_WORD = "/help"
    val HELP_MESSAGE = "The program calculates the sum of numbers. It can also handle + and -."
    val END_MESSAGE = "Bye!"
    val UNKNOWN_CMD = "Unknown command"
    val invalidExpression = "Invalid expression"
    val validString = Regex("^[-+\\d\\s]*\$")

    while (true) {
        val action = readln().replace(" ", "").replace("[", "").replace("]", "")
        when {
            action.isEmpty() -> continue
            action.startsWith(SAFE_WORD) -> break
            action.startsWith(HELP_WORD) -> println(HELP_MESSAGE)
            action.startsWith("/") -> println(UNKNOWN_CMD)
            !validString.matches(action) || action.endsWith("+") || action.endsWith("-") || !checkOccurrences(action) -> println(invalidExpression)
            else -> {
                val parsedAction = parseInput(action)
                println(parsedAction.trim().split(" ").sumOf { it.toInt() })
                }
            }
        }
    println(END_MESSAGE)
}
*/