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

        val corrupted = mutableSetOf<Pair<Int,Int>>()
        for (s in input.indices) {
            if (s >= 1024) break
            corrupted.add(split(input[s]))
        }

        val maxRow = 70
        val maxCol = 70

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
                if (!visited.contains(newPos) && (newPos.first >= 0) && (newPos.first <= maxRow)
                    && (newPos.second >= 0) && (newPos.second <= maxCol) && !corrupted.contains(newPos)) {
                    visited.add(newPos)
                    q.add(QueueEntry(newPos, nextIt))
                }
            }
        }

        return 9999
    }

    fun part2(input: List<String>): String {
        val corruptedList = input.map { split(it) }

        val maxRow = 70
        val maxCol = 70

        // Start counter at 1024 (12 in the example)
        for (c in 1024..< corruptedList.size) {

            val corrupted = mutableSetOf<Pair<Int,Int>>()
            for (s in input.indices) {
                if (s >= c) break
                corrupted.add(split(input[s]))
            }

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
                    if (!visited.contains(newPos) && (newPos.first >= 0) && (newPos.first <= maxRow)
                        && (newPos.second >= 0) && (newPos.second <= maxCol) && !corrupted.contains(newPos)
                    ) {
                        visited.add(newPos)
                        q.add(QueueEntry(newPos, nextIt))
                    }
                }
            }
            // No route
            if (!foundRoute) return input[c - 1]
            // Otherwise drop the next one
        }


        return "99999"
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
