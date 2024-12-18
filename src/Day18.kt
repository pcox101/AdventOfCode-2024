import java.util.*
import kotlin.time.measureTime

fun main() {

    val directions = arrayOf(Pair(0,1), Pair(-1,0), Pair(0,-1), Pair(1,0))

    operator fun Pair<Int,Int>.plus(o: Pair<Int,Int>): Pair<Int,Int> {
        return Pair(this.first + o.first, this.second + o.second)
    }

    fun split(i: String): Pair<Int,Int> {
        val v = i.split(",")
        return Pair(v[0].toInt(),v[1].toInt())
    }

    data class QueueEntry(val pos: Pair<Int,Int>, val nanosecond: Int)

    fun part1(input: List<String>): Int {

        val corrupted = mutableMapOf<Pair<Int,Int>,Int>()
        for (s in input.indices) {
            corrupted[split(input[s])] = s
        }

        val maxRow = 70
        val maxCol = 70

        val checkLessThan = 1024

        // BFS
        val visited = mutableSetOf<Pair<Int,Int>>()
        val q = LinkedList<QueueEntry>()
        q.add(QueueEntry(Pair(0,0), 0))
        visited.add(Pair(0,0))

        while (q.isNotEmpty()) {
            val entry = q.remove()

            if ((entry.pos.first == maxRow) && (entry.pos.second == maxCol)) {
                return entry.nanosecond
            }

            val nextIt = entry.nanosecond + 1
            //if ((nextIt % 100) == 0) println(nextIt)

            for (dir in directions) {
                val newPos = entry.pos + dir
                val corruptedAt = corrupted[newPos]
                if ((corruptedAt != null) && (corruptedAt < checkLessThan)) continue
                if (!visited.contains(newPos) && (newPos.first >= 0) && (newPos.first <= maxRow)
                    && (newPos.second >= 0) && (newPos.second <= maxCol)) {
                    visited.add(newPos)
                    q.add(QueueEntry(newPos, nextIt))
                }
            }
        }

        return 9999
    }

    fun part2(input: List<String>): String {

        val maxRow = 70
        val maxCol = 70

        val corrupted = mutableMapOf<Pair<Int,Int>,Int>()
        for (s in input.indices) {
            corrupted[split(input[s])] = s
        }

        // Use a binary search
        var highestFoundRoute = 1024
        var lowestNotFoundRoute = corrupted.size
        while (true)
        {
            val testPos = ((lowestNotFoundRoute - highestFoundRoute) / 2) + highestFoundRoute
            // BFS
            val visited = mutableSetOf<Pair<Int, Int>>()
            val q = LinkedList<QueueEntry>()
            q.add(QueueEntry(Pair(0, 0), 0))
            visited.add(Pair(0, 0))

            var foundRoute = false

            while (q.isNotEmpty()) {
                val entry = q.remove()

                if ((entry.pos.first == maxRow) && (entry.pos.second == maxCol)) {
                    foundRoute = true
                    break
                }

                val nextIt = entry.nanosecond + 1

                for (dir in directions) {
                    val newPos = entry.pos + dir
                    val corruptedAt = corrupted[newPos]
                    if ((corruptedAt != null) && (corruptedAt < testPos)) continue
                    if (!visited.contains(newPos) && (newPos.first >= 0) && (newPos.first <= maxRow)
                        && (newPos.second >= 0) && (newPos.second <= maxCol)) {
                        visited.add(newPos)
                        q.add(QueueEntry(newPos, nextIt))
                    }
                }
            }
            // Did we find a route?
            if (foundRoute) {
                highestFoundRoute = testPos
            }
            else {
                lowestNotFoundRoute = testPos
            }
            // If they are 1 apart, we have found the first one, otherwise loop again
            if (highestFoundRoute == lowestNotFoundRoute - 1) return input[lowestNotFoundRoute - 1]
        }
    }

    val input = readInput("Day18")
    var timeTaken = measureTime {
        part1(input).println()
    }
    println(timeTaken)

    timeTaken = measureTime {
        part2(input).println()
    }
    println(timeTaken)
}
