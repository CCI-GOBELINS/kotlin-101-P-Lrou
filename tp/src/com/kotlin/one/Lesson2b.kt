package com.android.one

fun main() {

    println("🧩 Kotlin List Processing Playground\n")

    println("Ex1 ImmutableList: ${ex1CreateImmutableList()}")
    println("Ex2 MutableList: ${ex2CreateMutableList()}")
    println("Ex3 FilterEven: ${ex3FilterEvenNumbers()}")
    println("Ex4 FilterAndMapAges: ${ex4FilterAndMapAges()}")
    println("Ex5 Flatten: ${ex5FlattenList()}")
    println("Ex6 FlatMapWords: ${ex6FlatMapWords()}")

    println("\n--- Eager Processing ---")
    println("Result: ${ex7EagerProcessing()}")

    println("\n--- Lazy Processing ---")
    println("Result: ${ex8LazyProcessing()}")

    println("\nEx9 FilterAndSortNames: ${ex9FilterAndSortNames()}")
}


// ✅ Exercise 1 — Immutable List
fun ex1CreateImmutableList(): List<Int> {
    return listOf(1, 2, 3, 4, 5)
}


// ✅ Exercise 2 — Mutable List
fun ex2CreateMutableList(): List<String> {
    val list = mutableListOf("Apple", "Banana", "Orange")
    list.add("Mango")
    return list
}


// ✅ Exercise 3 — Filter Even
fun ex3FilterEvenNumbers(): List<Int> {
    return (1..10).filter { it % 2 == 0 }
}


// ✅ Exercise 4 — Filter and Map
fun ex4FilterAndMapAges(): List<String> {
    val ages = listOf(12, 18, 25, 16, 30)
    return ages
        .filter { it >= 18 }
        .map { "Adult: $it" }
}


// ✅ Exercise 5 — Flatten Nested Lists
fun ex5FlattenList(): List<Int> {
    val nested = listOf(
        listOf(1, 2),
        listOf(3, 4),
        listOf(5)
    )
    return nested.flatten()
}


// ✅ Exercise 6 — FlatMap
fun ex6FlatMapWords(): List<String> {
    val phrases = listOf("Kotlin is fun", "I love lists")
    return phrases.flatMap { it.split(" ") }
}


// ✅ Exercise 7 — Eager Processing
fun ex7EagerProcessing(): List<Int> {

    val start = System.currentTimeMillis()

    val result = (1..1_000_000)
        .filter { it % 3 == 0 }
        .map { it * it }
        .take(5)

    val end = System.currentTimeMillis()
    println("Time: ${end - start} ms")

    return result
}


// ✅ Exercise 8 — Lazy Processing
fun ex8LazyProcessing(): List<Int> {

    val start = System.currentTimeMillis()

    val result = (1..1_000_000)
        .asSequence()
        .filter { it % 3 == 0 }
        .map { it * it }
        .take(5)
        .toList()

    val end = System.currentTimeMillis()
    println("Time: ${end - start} ms")

    return result
}


// ✅ Exercise 9 — Chain Multiple Operations
fun ex9FilterAndSortNames(): List<String> {
    val names = listOf("Alice", "Bob", "Anna", "Charlie", "Andrew", "David")

    return names
        .filter { it.startsWith("A") }
        .map { it.uppercase() }
        .sorted()
}