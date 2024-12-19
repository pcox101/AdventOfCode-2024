import kotlin.math.abs
import kotlin.time.measureTime

fun main() {
    fun part1(input: List<String>): Int {
        val rx = Regex("^(\\d*)\\s*(\\d*)\$")
        var diff  = 0

        val l1 = mutableListOf<Int>()
        val l2 = mutableListOf<Int>()

        for (s in input) {
            val matches = rx.matchEntire(s)

            if (matches != null) {
                l1.add(matches.groupValues[1].toInt())
                l2.add(matches.groupValues[2].toInt())
            }
            else
            {
                println("Invalid string")
            }
        }

        l1.sort()
        l2.sort()

        for (i in l1.indices)
        {
            diff += abs(l1[i] - l2[i])
        }

        return diff
    }

    fun part2(input: List<String>): Int {
        val rx = Regex("^(\\d*)\\s*(\\d*)\$")

        val l1 = mutableListOf<Int>()
        val l2 = mutableMapOf<Int,Int>()

        for (s in input) {
            val matches = rx.matchEntire(s)

            if (matches != null) {
                val v1 = matches.groupValues[1].toInt()
                val v2 = matches.groupValues[2].toInt()
                l1.add(v1)
                if (l2.containsKey(v2)) l2[v2] = l2[v2]!! + 1
                else l2[v2] = 1
            } else {
                println("Invalid string")
            }
        }

        var similarity = 0
        for (i in l1) {
            val thisScore = l2[i]
            if (thisScore != null) similarity += i * thisScore
        }

        return similarity
    }

    val input = readInput("Day01")
    var timeTaken = measureTime {
        part1(input).println()
    }
    println(timeTaken)

    timeTaken = measureTime {
        part2(input).println()
    }
    println(timeTaken)
}
