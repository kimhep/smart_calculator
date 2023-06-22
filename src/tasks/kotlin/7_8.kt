package calculator

import java.lang.Exception
import java.math.BigInteger
import java.util.*


val Variables = mutableMapOf<String, BigInteger>()

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
    val splitString =
        string.split(Regex("""(?<=[+\-*/^()])|(?=[+\-*/^()])""")).filter { it.matches(Regex("""^[a-zA-Z]+$""")) }
    var evalString = string
    try {
        for (element in splitString) {
            evalString = evalString.replace(element, Variables.getValue(element).toString())
        }
    } catch (e: java.util.NoSuchElementException) {
        println("Unknown variable")
        return ""
    }
    return evalString
}

fun handlePlusMinus(expression: String): BigInteger {
        val simplifiedExpression = StringBuilder()
        var stackCount = 0
        var previousChar: Char? = null

        for (i in expression.indices) {
            val c = expression[i]
            if (c == '-') {
                stackCount++
            } else if (c == '+') {
                // Ignore multiple consecutive '+' operators following one another
                if (previousChar != '+' && previousChar != '-' && expression[i+1] != '-') {
                    simplifiedExpression.append(c)
                }
            } else {
                if (stackCount > 0) {
                    simplifiedExpression.append(if (stackCount % 2 == 0) "+" else "-")
                    stackCount = 0
                }
                simplifiedExpression.append(c)
            }
            previousChar = c
        }

    val outputQueue = getRPN(simplifiedExpression.toString())
    return processRPN(outputQueue)
}

fun assignVariable(variableStatement: String) {
    val assignmentPair = Pair(variableStatement.split("=")[0], variableStatement.split("=")[1])
    if (Variables.containsKey(assignmentPair.second)) {
        Variables[assignmentPair.first] = Variables.getValue(assignmentPair.second)
    }
    else if (assignmentPair.second.toBigIntegerOrNull() != null) {
        Variables[assignmentPair.first] = assignmentPair.second.toBigInteger()
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
        println(handlePlusMinus(input).toString())
}

// https://brilliant.org/wiki/shunting-yard-algorithm/
fun getRPN(string: String): List<String> {
    val queue = mutableListOf<String>()
    val operatorStack = Stack<String>()
    val tokens = string.split(Regex("""(?<=[+\-*/^()])|(?=[+\-*/^()])"""))
    val operatorMap = mapOf<String, Int>(")" to -1, "+" to 0, "-" to 0, "*" to 1, "/" to 1, "^" to 2)

    var previousToken: String? = null

    for (token in tokens) {
        if (token.toBigIntegerOrNull() != null) {
            queue.add(token)
        } else if (token == "(") {
            operatorStack.push(token)
        } else if (token == ")") {
            try {
                while (operatorStack.isNotEmpty() && operatorStack.peek() != "(") {
                    queue.add(operatorStack.pop())
                }
                operatorStack.pop() // Pop the opening parenthesis from the stack
            } catch (e: EmptyStackException) {
                println("Missing opening parenthesis")
                return mutableListOf<String>()
            }
        } else if (operatorMap.containsKey(token)) {
            if (token == "-" && (previousToken == null || previousToken in operatorMap.keys && previousToken != ")")) {
                // Unary negation operator
                operatorStack.push("-")
            } else {
                while (operatorStack.isNotEmpty() && operatorStack.peek() != "(" && operatorMap.getValue(operatorStack.peek()) >= operatorMap.getValue(token)) {
                    queue.add(operatorStack.pop())
                }
                operatorStack.push(token)
            }
        }
        previousToken = token
    }

    while (operatorStack.isNotEmpty()) {
        queue.add(operatorStack.pop())
    }

    return queue
}


//  https://mathworld.wolfram.com/ReversePolishNotation.html
fun processRPN(rpn: List<String>): BigInteger {
    val stack = Stack<String>()
    val operatorMap = mapOf("+" to 0, "-" to 0, "*" to 1, "/" to 1, "^" to 2)

    for (token in rpn) {
        if (token.toBigIntegerOrNull() != null) {
            stack.push(token)
        } else if (operatorMap.containsKey(token.toString())) {

            var secOp = stack.pop().toBigInteger()
            var firstOp = stack.pop().toBigInteger()

            val result = when (token.toString()) {
                "+" -> firstOp + secOp
                "-" -> firstOp - secOp
                "*" -> firstOp * secOp
                "/" -> firstOp / secOp
                "^" -> firstOp.pow(secOp.toInt())
                else -> throw Exception("No")
            }
            stack.push(result.toString())
        }
    }

    return stack.pop().toBigInteger()
}

fun main() {
    val END_MESSAGE = "Bye!"
    val invalidExpression = "Invalid expression"
    val validString = Regex("^[-+a-zA-Z\\d\\s\\*\\(\\)\\/]*\$")

    while (true) {
        var action = readln().replace(" ", "").replace("[", "").replace("]", "")
        when {
            action.isEmpty() -> continue
            action.startsWith('/') -> if (checkCommand(action)) break else continue
            action.contains('=') -> processAssignment(action)
            (action.contains('(') && !action.contains(')'))|| action.contains(')') && !action.contains('(')  -> println(invalidExpression)
            Regex("[*/^]{2,}").find(action) != null -> println(invalidExpression)
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
