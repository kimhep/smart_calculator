package calculator

fun main() {
    var safe_word = "/exit"
    var helpWord = "/help"
    var exit = false
    var helpMessage = "The program calculates the sum of numbers"
    var endMessage = "Bye!"
    var shouldPrintProduct = false


    while (!exit){
        val input: List<String> = readLine()!!.split(" ").map { it.trim()}
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
                println()
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