package com.android.one

import kotlin.system.exitProcess

/**
 * Battle Arena (console prototype)
 * - Two players
 * - Each player creates a team of 3 characters
 * - Fight turn by turn until one team is fully dead
 *
 * Documentation is in English as requested.
 */

fun main() {
    println("🕹️  Battle Arena (Console Prototype)")
    println("===================================")
    println()

    val game = Game()
    game.start()
}

/* ----------------------------- Game Orchestration ----------------------------- */

private class Game {

    private val globalUsedNames = mutableSetOf<String>()
    private var turnCount: Int = 0

    fun start() {
        val player1 = createPlayer(playerNumber = 1)
        val player2 = createPlayer(playerNumber = 2)

        println("\n✅ Teams are ready! Let the battle begin!\n")
        fightLoop(player1, player2)
        endGame(player1, player2)
    }

    private fun createPlayer(playerNumber: Int): Player {
        println("👤 Player $playerNumber, enter your name:")
        val playerName = readNonBlankLine()

        val player = Player(name = playerName)

        val availableTypes = CharacterType.entries.toMutableSet()

        println("\n$playerName: Create your team (3 characters).")
        println("Type must be unique within your team. Name must be unique globally.\n")

        repeat(3) { index ->
            println("---- Character ${index + 1}/3 ----")
            val type = pickCharacterType(availableTypes)
            val name = pickUniqueCharacterName()

            val character = CharacterFactory.create(type = type, name = name)
            player.addCharacter(character)

            availableTypes.remove(type)

            println("✅ Created: ${character.summary()}")
            println()
        }

        println("📋 $playerName team:")
        player.printTeam()
        println()

        return player
    }

    private fun pickCharacterType(available: Set<CharacterType>): CharacterType {
        println("Choose a character type:")
        available.forEachIndexed { idx, t ->
            println("${idx + 1}. ${t.displayName} (HP=${t.baseHp}, Weapon=${t.weaponName}, Power=${t.weaponPower})")
        }

        while (true) {
            print("> ")
            val choice = readLine()?.trim()?.toIntOrNull()
            if (choice != null && choice in 1..available.size) {
                return available.elementAt(choice - 1)
            }
            println("Invalid choice. Please enter a number between 1 and ${available.size}.")
        }
    }

    private fun pickUniqueCharacterName(): String {
        println("Enter a unique name for this character:")
        while (true) {
            print("> ")
            val name = readNonBlankLine()
            if (name in globalUsedNames) {
                println("Name already used in this game. Choose another one.")
                continue
            }
            globalUsedNames.add(name)
            return name
        }
    }

    private fun fightLoop(p1: Player, p2: Player) {
        var active = p1
        var opponent = p2

        while (p1.hasLivingCharacters() && p2.hasLivingCharacters()) {
            turnCount++
            println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
            println("🔁 Turn #$turnCount — ${active.name}'s turn")
            println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")

            println("\n${active.name}'s team:")
            active.printTeam()

            println("\n${opponent.name}'s team:")
            opponent.printTeam()

            val actor = selectLivingCharacter(active, prompt = "\nChoose your character:")
            val action = selectAction(actor)

            when (action) {
                Action.ATTACK -> {
                    val target = selectLivingCharacter(opponent, prompt = "Choose an enemy to attack:")
                    val damage = actor.attack(target)
                    println("💥 ${actor.name} attacked ${target.name} for $damage damage.")
                    if (!target.isAlive()) {
                        println("☠️  ${target.name} has died.")
                    }
                }

                Action.HEAL -> {
                    if (actor !is Healer) {
                        // This should not happen because action menu filters it, but keep it safe.
                        println("❌ This character cannot heal.")
                    } else {
                        val ally = selectLivingCharacter(active, prompt = "Choose an ally to heal:")
                        val healed = actor.heal(ally)
                        println("✨ ${actor.name} healed ${ally.name} for $healed HP.")
                    }
                }
            }

            // Swap turns
            val tmp = active
            active = opponent
            opponent = tmp

            println()
        }
    }

    private fun endGame(p1: Player, p2: Player) {
        val winner = if (p1.hasLivingCharacters()) p1 else p2
        val loser = if (winner == p1) p2 else p1

        println("🏁 GAME OVER")
        println("Winner: ${winner.name}")
        println("Turns played: $turnCount")
        println()

        println("📌 Final status — ${winner.name}:")
        winner.printTeam()
        println()

        println("📌 Final status — ${loser.name}:")
        loser.printTeam()
        println()
    }

    private fun selectLivingCharacter(player: Player, prompt: String): Character {
        println(prompt)
        val living = player.livingCharacters()
        living.forEachIndexed { idx, c ->
            println("${idx + 1}. ${c.summary()}")
        }

        while (true) {
            print("> ")
            val choice = readLine()?.trim()?.toIntOrNull()
            if (choice != null && choice in 1..living.size) {
                return living[choice - 1]
            }
            println("Invalid choice. Please enter a number between 1 and ${living.size}.")
        }
    }

    private fun selectAction(actor: Character): Action {
        val possible = mutableListOf(Action.ATTACK)
        if (actor is Healer) possible.add(Action.HEAL)

        println("\nChoose an action for ${actor.name}:")
        possible.forEachIndexed { idx, a -> println("${idx + 1}. ${a.displayName}") }

        while (true) {
            print("> ")
            val choice = readLine()?.trim()?.toIntOrNull()
            if (choice != null && choice in 1..possible.size) {
                return possible[choice - 1]
            }
            println("Invalid choice. Please enter a number between 1 and ${possible.size}.")
        }
    }
}

/* --------------------------------- Domain Model -------------------------------- */

private enum class Action(val displayName: String) {
    ATTACK("Attack"),
    HEAL("Heal")
}

private enum class CharacterType(
    val displayName: String,
    val baseHp: Int,
    val weaponName: String,
    val weaponPower: Int
) {
    WARRIOR(displayName = "Warrior", baseHp = 120, weaponName = "Sword", weaponPower = 25),
    MAGUS(displayName = "Magus", baseHp = 140, weaponName = "Staff", weaponPower = 12),
    COLOSSUS(displayName = "Colossus", baseHp = 180, weaponName = "Shield Bash", weaponPower = 18),
    DWARF(displayName = "Dwarf", baseHp = 90, weaponName = "Axe", weaponPower = 35),
}

private data class Weapon(val name: String, val power: Int)

/**
 * Interface for characters that can heal.
 */
private interface Healer {
    fun heal(target: Character): Int
}

/**
 * Abstract class for polymorphism + encapsulation.
 */
private abstract class Character(
    val name: String,
    val type: CharacterType,
    val weapon: Weapon,
    baseHp: Int
) {
    // Encapsulation: HP can only be changed through methods.
    private var hp: Int = baseHp

    fun isAlive(): Boolean = hp > 0
    fun hp(): Int = hp

    fun takeDamage(amount: Int) {
        if (!isAlive()) return
        hp = (hp - amount).coerceAtLeast(0)
    }

    fun receiveHeal(amount: Int) {
        if (!isAlive()) return
        // Prototype: no max HP cap to keep rules simple.
        hp += amount
    }

    open fun attack(target: Character): Int {
        val damage = weapon.power
        target.takeDamage(damage)
        return damage
    }

    fun summary(): String {
        val status = if (isAlive()) "ALIVE" else "DEAD"
        return "$name (${type.displayName}) — HP=${hp()} — Weapon=${weapon.name}(${weapon.power}) — $status"
    }
}

/* ------------------------------ Concrete Characters ----------------------------- */

private class Warrior(name: String) : Character(
    name = name,
    type = CharacterType.WARRIOR,
    weapon = Weapon(CharacterType.WARRIOR.weaponName, CharacterType.WARRIOR.weaponPower),
    baseHp = CharacterType.WARRIOR.baseHp
)

private class Colossus(name: String) : Character(
    name = name,
    type = CharacterType.COLOSSUS,
    weapon = Weapon(CharacterType.COLOSSUS.weaponName, CharacterType.COLOSSUS.weaponPower),
    baseHp = CharacterType.COLOSSUS.baseHp
)

private class Dwarf(name: String) : Character(
    name = name,
    type = CharacterType.DWARF,
    weapon = Weapon(CharacterType.DWARF.weaponName, CharacterType.DWARF.weaponPower),
    baseHp = CharacterType.DWARF.baseHp
)

private class Magus(name: String) : Character(
    name = name,
    type = CharacterType.MAGUS,
    weapon = Weapon(CharacterType.MAGUS.weaponName, CharacterType.MAGUS.weaponPower),
    baseHp = CharacterType.MAGUS.baseHp
), Healer {

    override fun heal(target: Character): Int {
        // Prototype heal amount (could be tied to weapon or a dedicated "spell power").
        val healAmount = 22
        target.receiveHeal(healAmount)
        return healAmount
    }
}

/* ------------------------------ Factory + Player ------------------------------ */

private object CharacterFactory {
    fun create(type: CharacterType, name: String): Character {
        return when (type) {
            CharacterType.WARRIOR -> Warrior(name)
            CharacterType.MAGUS -> Magus(name)
            CharacterType.COLOSSUS -> Colossus(name)
            CharacterType.DWARF -> Dwarf(name)
        }
    }
}

private class Player(val name: String) {
    private val team = mutableListOf<Character>()

    fun addCharacter(character: Character) {
        team.add(character)
    }

    fun hasLivingCharacters(): Boolean = team.any { it.isAlive() }

    fun livingCharacters(): List<Character> = team.filter { it.isAlive() }

    fun printTeam() {
        team.forEachIndexed { idx, c ->
            println("${idx + 1}. ${c.summary()}")
        }
    }
}

/* ---------------------------------- Helpers ---------------------------------- */

private fun readNonBlankLine(): String {
    while (true) {
        val line = readLine()?.trim()
        if (!line.isNullOrBlank()) return line
        println("Please enter a non-empty value.")
    }
}