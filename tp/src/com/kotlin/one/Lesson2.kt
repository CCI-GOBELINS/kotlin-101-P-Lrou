package com.android.one

fun greet(name: String = "Student"): String {
    // The test expects the function to return the provided name (or default).
    return name
}

fun printInfo(name: String, age: Int = 18, city: String = "Paris") {
    // Print exactly in the requested format.
    println("$name is $age years old and lives in $city.")
}

fun add(a: Int, b: Int): Int {
    return a + b
}

fun isEven(number: Int): Boolean {
    return number % 2 == 0
}

fun areaOfCircle(radius: Double): Double {
    // Area = π * r²
    return Math.PI * radius * radius
}

// TODO 5: Return a letter grade based on score.
fun grade(score: Int): String {
    return when {
        score >= 90 -> "A"
        score >= 80 -> "B"
        score >= 70 -> "C"
        score >= 60 -> "D"
        else -> "F"
    }
}

fun maxOfThree(a: Int, b: Int, c: Int): Int {
    return maxOf(maxOf(a, b), c)
}

fun toFahrenheit(celsius: Double): Double {
    // F = C * 9/5 + 32
    return celsius * 9.0 / 5.0 + 32.0
}

fun applyDiscount(price: Double, discount: Double = 0.1): Double {
    // Default discount is 10%
    return price * (1.0 - discount)
}

fun capitalizeWords(sentence: String): String {
    // Capitalize the first letter of each word (handles extra spaces).
    return sentence
        .trim()
        .split(Regex("\\s+"))
        .filter { it.isNotEmpty() }
        .joinToString(" ") { word ->
            word.replaceFirstChar { ch ->
                if (ch.isLowerCase()) ch.titlecase() else ch.toString()
            }
        }
}

fun bmi(weight: Double, height: Double): Double {
    // BMI = weight / height²
    return weight / (height * height)
}

fun passwordStrength(password: String): Boolean {
    val hasMinLength = password.length >= 8
    val hasUppercase = password.any { it.isUpperCase() }
    val hasLowercase = password.any { it.isLowerCase() }
    val hasNumber = password.any { it.isDigit() }

    return hasMinLength && hasUppercase && hasLowercase && hasNumber
}

fun filterEvenNumbers(numbers: List<Int>): List<Int> {
    return numbers.filter { it % 2 == 0 }
}

fun factorial(n: Int): Int {
    // Factorial recursively: 0! = 1 and 1! = 1
    require(n >= 0) { "n must be >= 0" }
    return if (n <= 1) 1 else n * factorial(n - 1)
}

fun fibonacci(n: Int): Int {
    // Fibonacci recursively with F(0)=0, F(1)=1
    require(n >= 0) { "n must be >= 0" }
    return when (n) {
        0 -> 0
        1 -> 1
        else -> fibonacci(n - 1) + fibonacci(n - 2)
    }
}

// TODO 19: Simple calculator using when expression.
fun miniCalculator() {
    println("Enter first number:")
    val a = readln().toDouble()

    println("Enter second number:")
    val b = readln().toDouble()

    println("Enter operator (+, -, *, /):")
    val op = readln().trim()

    val result = when (op) {
        "+" -> a + b
        "-" -> a - b
        "*" -> a * b
        "/" -> a / b
        else -> {
            println("Unknown operator: $op")
            return
        }
    }

    println("Result: $result")
}

// TODO 20: Text analyzer.
fun analyzeText(text: String): Map<String, Any> {
    val charCount = text.length

    val words = text.trim()
        .split(Regex("\\s+"))
        .filter { it.isNotBlank() }

    val wordCount = words.size
    val longestWord = words.maxByOrNull { it.length } ?: ""

    val totalLetters = words.sumOf { it.length }
    val averageWordLength = if (wordCount == 0) 0.0 else totalLetters.toDouble() / wordCount.toDouble()

    return mapOf(
        "charCount" to charCount,
        "wordCount" to wordCount,
        "longestWord" to longestWord,
        "averageWordLength" to averageWordLength
    )
}

fun main() {
    println("🔍 Running Kotlin Functions Playground Tests...\n")

    var passed = 0
    var failed = 0

    fun verify(name: String, block: () -> Boolean) {
        try {
            check(block()) { "❌ Test failed: $name" }
            println("✅ $name")
            passed++
        } catch (e: Throwable) {
            println("❌ $name → ${e.message}")
            failed++
        }
    }

    // 🟢 LEVEL 1
    verify(name = "greet() with default") { greet() == "Student" }
    verify(name = "greet(\"Alice\")") { greet("Alice") == "Alice" }
    verify("printInfo with all defaults") {
        printInfo("Bob")
        true // Just checking it runs without error
    }
    verify("add(3,5) == 8") { add(3, 5) == 8 }
    verify("isEven(4) == true") { isEven(4) }
    verify("isEven(7) == false") { !isEven(7) }
    verify("areaOfCircle(2.0) ≈ 12.57") {
        val result = areaOfCircle(2.0)
        result in 12.56..12.58
    }

    // 🟡 LEVEL 2
    verify("grade(95) == 'A'") { grade(95) == "A" }
    verify("grade(82) == 'B'") { grade(82) == "B" }
    verify("maxOfThree(3,9,6) == 9") { maxOfThree(3, 9, 6) == 9 }
    verify("toFahrenheit(20.0) == 68.0") { (toFahrenheit(20.0) - 68.0).absoluteValue < 0.1 }

    // 🟠 LEVEL 3
    verify("applyDiscount(100.0) == 90.0") { (applyDiscount(100.0) - 90.0).absoluteValue < 0.001 }
    verify("applyDiscount(100.0, 0.2) == 80.0") { (applyDiscount(100.0, 0.2) - 80.0).absoluteValue < 0.001 }

    // 🟣 LEVEL 4
    verify("capitalizeWords works") { capitalizeWords("hello kotlin world") == "Hello Kotlin World" }
    verify("bmi(70,1.75) ≈ 22.86") { bmi(70.0, 1.75) in 22.8..22.9 }
    verify("passwordStrength detects strong") { passwordStrength("MyPass123") }
    verify("passwordStrength detects weak") { !passwordStrength("weak") }
    verify("filterEvenNumbers works") {
        filterEvenNumbers(listOf(1, 2, 3, 4, 5, 6)) == listOf(2, 4, 6)
    }

    // ⚫ LEVEL 5
    verify("factorial(5) == 120") { factorial(5) == 120 }
    verify("fibonacci(6) == 8") { fibonacci(6) == 8 }

    // 🧠 LEVEL 7
    verify("analyzeText stats") {
        val result = analyzeText("Kotlin is fun and powerful")
        result["charCount"] == 26 &&
                result["wordCount"] == 5 &&
                result["longestWord"] == "powerful" &&
                (result["averageWordLength"] as Double) in 4.0..5.0
    }

    println("\n🎯 TEST SUMMARY: $passed passed, $failed failed.")
    if (failed == 0) println("🎉 All tests passed! Great job!")
    else println("⚠️  Some tests failed. Keep debugging!")
}

// Simple helper for double comparison
private val Double.absoluteValue get() = if (this < 0) -this else this