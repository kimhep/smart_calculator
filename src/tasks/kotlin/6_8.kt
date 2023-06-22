import java.lang.Exception
import java.util.*


val Variables = mutableMapOf<String, Int>()

fun checkOccurrences(string: String): Boolean {
    val plusMinusCount = Regex("[+-]").findAll(string).count()
    val digitCount = string.replace("+", " ").replace("-", " ").split(" ").size
    return plusMinusCount == digitCount - 1
}

fun checkCommand(cmd: String): Boolean{  // 1 is continuing, 2 is go into parser and 3 is exiting
    val SAFE_WORD = "/exit"
    val HELP_WORD = "/help"
    val HELP_MESSAGE = "The program calculates the sum of numbers. It can also handle + and -."
    val UNKNOWN_CMD = "Unknown command"
    when {
        cmd.startsWith(SAFE_WORD)  -> {
            return true
        }
        cmd.startsWith(HELP_WORD) -> {
            println(HELP_MESSAGE)
        }
        else -> println(UNKNOWN_CMD)
        }
    return false
    }

fun replaceVariables(string: String): String {
    val splitedString =
        string.split(Regex("""(?<=[+\-*/^()])|(?=[+\-*/^()])""")).filter { it.matches(Regex("""^[a-zA-Z]+$""")) }
    var evalString = string
    try {
        for (element in splitedString) {
            evalString = evalString.replace(element, Variables.getValue(element).toString())
        }
    } catch (e: java.util.NoSuchElementException) {
        println("Unknown variable")
        return ""
    }
    return evalString
}

fun handlePlusMinus(string: String): Int {
    var positive = true
    var lastCharInt = false
    var tempIntStr = ""
    var firstRun = true

    for (char in string) {
        if (char.digitToIntOrNull() != null) {
            if (lastCharInt) {
                tempIntStr += char
            }
            else {
                tempIntStr += if (positive && firstRun) "$char" else if (positive) "+$char" else ("-$char")
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
        else tempIntStr += char
        firstRun = false
    }

    var outputQueue = getRPN(tempIntStr)
    return processRPN(outputQueue)
}

fun assignVariable(variableStatement: String) {
    val assignmentPair = Pair(variableStatement.split("=")[0], variableStatement.split("=")[1])
    if (Variables.containsKey(assignmentPair.second)) {
        Variables[assignmentPair.first] = Variables.getValue(assignmentPair.second)
    }
    else if (assignmentPair.second.toIntOrNull() != null) {
        Variables[assignmentPair.first] = assignmentPair.second.toInt()
    }
    else {
        if (assignmentPair.second.matches(Regex("^[a-zA-Z]+\$"))) {
            println("Unknown variable")
        } else {
            println("Invalid assignment")
        }
    }
}

fun checkValidAssignment(input: String): Boolean {
    val invAssignment = "Invalid assignment"
    val invIdentifier = "Invalid identifier"
    when {
        !input.matches(Regex("^[^=]*=[^=]*"))  || !input.matches(Regex("""^.+\s*=\s*(-?[a-zA-Z]+|-?\d+(\.\d+)?)$""")) -> {
            println(invAssignment)
            return false
        } // more than one = OR if there is a mix of num and a-z after the = or there is no = and there is a mix of num and a-Z
        !input.matches(Regex("^[a-zA-Z]+=.*")) -> {
            println(invIdentifier)
            return false
        }
    }
    return true
}

fun processAssignment(variableStatement: String){
    if (checkValidAssignment(variableStatement)) assignVariable(variableStatement)
}

fun parseInput(input: String) {
    when{
        input.contains('+') || input.contains('-') -> println(handlePlusMinus(input).toString())
    }
}


// https://brilliant.org/wiki/shunting-yard-algorithm/
fun getRPN(string: String): List<String>  {
    val queue = mutableListOf<String>()
    val operatorStack = Stack<String>()
    var tokens = string.split(Regex("""(?<=[+\-*/^()])|(?=[+\-*/^()])"""))
    val operarotMap = mapOf<String, Int>("+" to 0, "-" to 0, "*" to 1, "/" to  1, "^" to 2)

    for (token in tokens){
        if (token.toIntOrNull() != null) {
            queue.add(token)
        }
        else if (operarotMap.containsKey(token.toString())) {
            if (operatorStack.size != 0) {
                while (operatorStack.isNotEmpty() && operarotMap.getValue(operatorStack.last().toString()) >= operarotMap.getValue(token.toString()))
                    queue.add(operatorStack.pop().toString())
            }
            operatorStack.push(token)
        }
        else if (token == "(") {
            operatorStack.push(token)
        }
        else if (token == ")") {
            while (operatorStack.last() != "(") queue.add(operatorStack.pop().toString())
            operatorStack.pop()
        }
    }
    while (operatorStack.isNotEmpty()) {
        queue.add(operatorStack.pop().toString())
    }
    return queue
}


//  https://mathworld.wolfram.com/ReversePolishNotation.html
fun processRPN(rpn: List<String>): Int {
    val stack = Stack<String>()
    val operatorMap = mapOf("+" to 0, "-" to 0, "*" to 1, "/" to 1, "^" to 2)

    for (token in rpn) {
        if (token.toIntOrNull() != null) {
            stack.push(token)
        } else if (operatorMap.containsKey(token.toString())) {

            var y = stack.pop().toBigInteger()
            var x = stack.pop().toBigInteger()

            val result = when (token.toString()) {
                "+" -> x + y
                "-" -> x - y
                "*" -> x * y
                "/" -> x / y
                "^" -> x.pow(y.toInt())
                else -> throw Exception("No")
            }
            stack.push(result.toString())
        }
    }

    return stack.pop().toInt()
}

fun main() {
    val END_MESSAGE = "Bye!"
    val invalidExpression = "Invalid expression"
    val validString = Regex("^[-+a-zA-Z\\d\\s]*\$")

    while (true) {
        var action = readln().replace(" ", "").replace("[", "").replace("]", "")
        when {
            action.isEmpty() -> continue
            action.startsWith('/') -> if (checkCommand(action)) break else continue
            action.contains('=') -> processAssignment(action)
            Variables.containsKey(action) -> println(Variables.getValue(action))
            !validString.matches(action) || action.endsWith("+") || action.endsWith("-") || !checkOccurrences(action) -> println(invalidExpression)
            else -> {
                if (action.matches(Regex(".*[a-zA-Z].*"))) action = replaceVariables(action)
                if (action.isNotEmpty()) parseInput(action)
            }
        }
    }
    println(END_MESSAGE)
}
