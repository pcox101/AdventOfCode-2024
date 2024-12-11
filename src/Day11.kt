import java.math.BigInteger
import kotlin.time.measureTime

fun main() {

    val cache = mutableMapOf<Pair<Long, Long>,Long>()

    fun calculateBlink(stone: Long, blinksRemaining: Long): Long
    {
        if (blinksRemaining == 0L) return 1L

        val cacheEntry = cache[Pair(stone, blinksRemaining)]
        if (cacheEntry != null)
            return cacheEntry

        val stoneAsString = stone.toString()
        if (stone == 0L) {
            val v = calculateBlink(1, blinksRemaining - 1 )
            cache.put(Pair(1, blinksRemaining - 1), v)
            return v
        }
        else if (stoneAsString.length.mod(2) == 0) {
            val half = stoneAsString.length / 2
            val s1 = stoneAsString.substring(0, half).toLong()
            val v1 = calculateBlink(s1, blinksRemaining - 1)
            cache.put(Pair(s1, blinksRemaining - 1), v1)
            val s2 = stoneAsString.substring(half, stoneAsString.length).toLong()
            val v2 = calculateBlink(s2, blinksRemaining - 1)
            cache.put(Pair(s2, blinksRemaining - 1), v2)
            return v1 + v2
        }
        else {
            val v = calculateBlink(stone * 2024, blinksRemaining - 1)
            cache.put(Pair(stone * 2024, blinksRemaining - 1), v)
            return v
        }
    }

    fun part1(input: List<String>): Long {
        val stones = input[0].split(" ").map { it.toLong() }

        var v = 0L
        for (stone in stones) {
            v += calculateBlink(stone, 25)
        }
        return v
    }

    fun part2(input: List<String>): Long {

        val stones = input[0].split(" ").map { it.toLong() }

        var v = 0L
        for (stone in stones) {
            v += calculateBlink(stone, 75)
        }
        return v
    }

    val input = readInput("Day11")
    var timeTaken = measureTime {
        part1(input).println()
    }
    println(timeTaken)

    timeTaken = measureTime {
        part2(input).println()
    }
    println(timeTaken)
}
