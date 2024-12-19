import kotlin.time.measureTime

fun main() {

    val memo1 = mutableMapOf<String, Boolean>()
    val memo2 = mutableMapOf<String, Long>()

    fun isPossible(design:String, patterns:List<String>): Boolean
    {
        if (design.isEmpty())
            return true

        val c = memo1[design]
        if (c != null) return c

        for (p in patterns) {
            if (p.length > design.length) continue

            if (design.substring(0,p.length) == p) {
                // Strip this pattern off
                val testDesign = design.substring(p.length, design.length)
                val isPossible = isPossible(testDesign, patterns)
                if (isPossible) {
                    memo1[design] = true
                    return true
                }
            }
        }

        memo1[design] = false
        return false
    }

    fun part1(input: List<String>): Int {
        val patterns = input[0].split(" ").map { it.split(",")[0] }

        var counter = 0
        for (p in 2..<input.size) {
            if (isPossible(input[p], patterns)) counter++

        }
        return counter
    }

    fun numberOfWays(design:String, patterns:List<String>): Long {
        if (design.isEmpty())
            return 1

        val c = memo2[design]
        if (c != null) return c

        var counter = 0L
        for (p in patterns) {
            if (p.length > design.length) continue

            if (design.substring(0, p.length) == p) {
                // Strip this pattern off
                val testDesign = design.substring(p.length, design.length)
                val numberOfWays = numberOfWays(testDesign, patterns)
                counter += numberOfWays
            }
        }
        memo2[design] = counter
        return counter
    }

    fun part2(input: List<String>): Long {
        val patterns = input[0].split(" ").map { it.split(",")[0] }

        var counter = 0L
        for (p in 2..<input.size) {
            counter += numberOfWays(input[p], patterns)

        }
        return counter
    }

    val input = readInput("Day19")
    var timeTaken = measureTime {
        part1(input).println()
    }
    println(timeTaken)

    timeTaken = measureTime {
        part2(input).println()
    }
    println(timeTaken)
}
