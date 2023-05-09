// The program must calculate expressions like these: 4 + 6 - 8, 2 - 3 - 4, and so on.
// Modify the result of the /help command to explain these operations.
// Decompose your program using functions to make it easy to understand and edit later.
// The program should not stop until the user enters the /exit command.
// If you encounter an empty line, do not output anything.

package calculator

// fun sum() {}

// fun sub() {}

// fun 


fun main() {
    var safe_word = "/exit"
    var helpWord = "/help"
    var exit = false
    var helpMessage = "The program calculates the sum of numbers"
    var endMessage = "Bye!"
    var shouldPrintProduct = false

    while (!exit){
        val input: List<String> = readLine()!!.split(" ").filter { it != null && !it.isNullOrBlank() }.map { it.trim() }
        // input.removeAll(listOf(null))
        println(input)
        var product = 0

        for (value in input){
            if (value.equals(safe_word, ignoreCase = false)) {
                shouldPrintProduct = false
                exit = true
                println(endMessage)
                break
            }
            else if (value.equals(helpWord, ignoreCase = false)) {
                shouldPrintProduct = false
                println(helpMessage)
                break
            }
            else if (value.toIntOrNull() != null) {
                product += value.toInt()
                shouldPrintProduct = true
            }
            else {
                shouldPrintProduct = false
                break
            }
        }
        if (shouldPrintProduct) {
            println(product)
        }
    }
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