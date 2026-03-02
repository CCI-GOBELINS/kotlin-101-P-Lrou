package com.android.one

fun main() {

    println("👋 Welcome to the Kotlin Playground!")
    println("Let's start learning step by step.\n")

    // ✅ EXERCISE 1 Variables:

    val city: String = "Paris"          // immutable
    var temperature: Double = 18.5      // mutable

    println("It is $temperature°C in $city")

    temperature = 22.0
    println("It is $temperature°C in $city")


    // ✅ EXERCISE 2 Conditionals:

    val score: Int = 75

    if (score == 100) {
        println("Perfect score!")
    } else if (score < 0 || score > 100) {
        println("Invalid score")
    } else if (score in 0..49) {
        println("You failed!")
    } else if (score in 50..60) {
        println("Just passed!")
    } else if (score in 61..99) {
        println("Well done!")
    }


    // ✅ EXERCISE 3 List and Loops:

    val fruits = listOf("Apple", "Banana", "Mango", "Strawberry")

    for (fruit in fruits) {
        println(fruit.uppercase())
    }

    println("Total number of fruits: ${fruits.size}")

    print("Enter a fruit name: ")
    val userInput = readLine()

    if (userInput != null && fruits.contains(userInput)) {
        println("$userInput is in the list.")
    } else {
        println("$userInput is not in the list.")
    }


    // ✅ EXERCISE 4 Elvis Operator:

    val nickname: String? = null

    val length = nickname?.length ?: 0
    println("Nickname length: $length")

    val displayName = nickname ?: "No nickname provided"
    println(displayName)
}