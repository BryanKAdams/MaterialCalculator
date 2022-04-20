package com.bryankeltonadams.materialcalculator

import java.math.BigDecimal
import java.math.RoundingMode

class InfixToPostfixAndEvaluateUtil {
    companion object {
        fun infixToPostfix(infix: String): String {
            val infixStack: ArrayDeque<Char> = ArrayDeque(listOf())
            var postfix = ""
            var i = 0
            var nextNegative = false
            while (i < infix.length) {
                val c = infix[i]
                if (c == '(') {
                    if (i - 1 >= 0 && infix[i - 1] != 'x' && (infix[i - 1] != '(' && infix[i - 1] != '+' && infix[i - 1] != '-' && infix[i - 1] != '/')) {
                        infixStack.addLast('x')

                    }
                    infixStack.addLast(c)
                } else if (c == ')') {
                    while (infixStack.last() != '(')
                        postfix += infixStack.removeLast()
                    postfix += ' '
                    if (infixStack.last() == '(') {
                        infixStack.removeLast()
                    }
                } else if (c in "+-") {
                    if (c == '-') {
                        if (i == 0 || (!infix[i - 1].isDigit() && infix[i - 1] != ')')) {
                            nextNegative = true
                            i++
                            continue
                        }
                    }
                    while (infixStack.isNotEmpty() && infixStack.last() in "+-x/%")
                        postfix += infixStack.removeLast()
                    infixStack.addLast(c)
                } else if (c in "x/%") {
                    while (infixStack.isNotEmpty() && infixStack.last() in "x/%")
                        postfix += infixStack.removeLast()
                    infixStack.addLast(c)
                } else if (c.isDigit()) {
                    if (i - 1 > 0 && infix[i - 1] == ')') {
                        infixStack.addLast('x')
                    }
                    val arrayOfDigits = arrayListOf(c)
                    while (i + 1 < infix.length && (infix[i + 1].isDigit() || infix[i + 1] == '.')) {
                        arrayOfDigits.add(infix[i + 1])
                        i++
                    }
                    var stringOfDigits = ""
                    for (item in arrayOfDigits) {
                        stringOfDigits += item
                    }
                    if (nextNegative) {
                        postfix += '-'
                        nextNegative = false
                    }
                    postfix += stringOfDigits
                    postfix += ' '


                } else if (c == '.') {
                    if (nextNegative) {
                        postfix += '-'
                        nextNegative = false
                    }
                    val arrayOfDigits = arrayListOf(c)
                    while (i + 1 < infix.length && infix[i + 1].isDigit()) {
                        arrayOfDigits.add(infix[i + 1])
                        i++
                    }
                    var stringOfDigits = ""
                    for (item in arrayOfDigits) {
                        stringOfDigits += item
                    }
                    postfix += stringOfDigits
                    postfix += ' '
                } else {
                    print("Bad character")
                }
                i++
            }
            while (infixStack.isNotEmpty()) {
                postfix += infixStack.removeLast()
                postfix += ' '
            }
            return postfix
        }

        fun evaluatePostfix(postfix: String): String {
            val postfixStack: ArrayDeque<BigDecimal> = ArrayDeque(listOf())
            var i = 0
            var nextNegative = false
            while (i < postfix.length) {
                val c = postfix[i]
                when {
                    c.isDigit() -> {
                        var string = ""
                        if (nextNegative) {
                            string += '-'
                            nextNegative = false
                        }
                        string += c
                        while (i + 1 < postfix.length && (postfix[i + 1] != ' ')) {
                            string += postfix[i + 1]
                            i += 1
                        }
                        postfixStack.addLast((string.toBigDecimal()))
                    }
                    c == '+' -> {
                        val rhs = postfixStack.removeLast()
                        val lhs = postfixStack.removeLast()
                        val addition = lhs.add(rhs)
                        postfixStack.addLast(addition)
                    }
                    c == '-' -> {
                        if (i == 0 || postfix[i + 1].isDigit()) {
                            nextNegative = true
                            i++
                            continue
                        }
                        val rhs = postfixStack.removeLast()
                        val lhs = postfixStack.removeLast()
                        val subtraction = lhs.subtract(rhs)
                        postfixStack.addLast(subtraction)
                    }
                    c == 'x' -> {
                        val rhs = postfixStack.removeLast()
                        val lhs = postfixStack.removeLast()
                        val multiplication = lhs.multiply(rhs)
                        postfixStack.addLast(multiplication)
                    }
                    c == '/' -> {
                        val rhs = postfixStack.removeLast()
                        val lhs = postfixStack.removeLast()
                        if (0 == "0".toBigDecimal().compareTo(rhs)) {
                            return "Infinity"
                        }
                        val division = lhs.divide(rhs, 10, RoundingMode.HALF_UP)
                        postfixStack.addLast(division)
                    }
                    c == '%' -> {
                        val rhs = postfixStack.removeLast()
                        val lhs = postfixStack.removeLast()
                        val mod = lhs.remainder(rhs)
                        postfixStack.addLast(mod)
                    }
                    c == '.' -> {
                        var string = ""
                        if (nextNegative) {
                            string += "-0"
                            nextNegative = false
                        }
                        string += c
                        while (i + 1 < postfix.length && (postfix[i + 1] != ' ')) {
                            string += postfix[i + 1]
                            i += 1
                        }
                        postfixStack.addLast((string.toBigDecimal()))
                    }

                    else -> {
                        print("Invalid character")
                    }
                }
                i++
            }
            return postfixStack.first().toString()
        }
    }

}