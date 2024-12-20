import java.util.*
import kotlin.math.abs
import kotlin.time.measureTime

fun main() {

    val directions = arrayOf(Pair(0,1), Pair(-1,0), Pair(1,0), Pair(0,-1))

    data class QueueEntry(val pos: Pair<Int,Int>,
                          val counter: Int,
                          val visited: List<Pair<Int,Int>>)

    fun breadthFirstSearch(gameBoard: Array<CharArray>,
                           startPos: Pair<Int,Int>,
                           endPos: Pair<Int,Int>): List<Pair<Int,Int>> {

        val q = LinkedList<QueueEntry>()

        var lastCounter = 0

        q.add(QueueEntry(startPos,0, listOf(startPos)))
        val visited = mutableSetOf<Pair<Int,Int>>()
        visited.add(startPos)

        while (q.isNotEmpty()) {
            val entry = q.remove()

            if (lastCounter != entry.counter) {
                //println("Moved to next at ${entry.counter} (Shortcut at is $shortCutAt).")
                lastCounter = entry.counter
            }

            if (entry.pos == endPos) {
                return entry.visited
            }

            // N/S/E/W
            for (dir in directions) {
                val nextPos = entry.pos + dir
                if ((nextPos.first < 0) || (nextPos.first >= gameBoard.size) || (nextPos.second < 0) || (nextPos.second >= gameBoard[0].size))
                    continue

                if (gameBoard[nextPos.first][nextPos.second] != '#') {
                    if (!visited.contains(nextPos)) {
                        visited.add(nextPos)
                        val newVisited = entry.visited.toMutableList()
                        newVisited.add(nextPos)
                        q.add(QueueEntry(nextPos, entry.counter + 1, newVisited))
                    }
                }
            }
        }

        return listOf()
    }

    fun getNumberOfPossibleCheats(
        baseBFS: List<Pair<Int, Int>>,
        maxCheat: Int,
        considerSaving: Int
    ): Int {
        val cheats = mutableSetOf<Pair<Pair<Int, Int>, Pair<Int, Int>>>()

        for (f in 0..<baseBFS.size - maxCheat) {
            for (s in f + maxCheat..<baseBFS.size) {
                val first = baseBFS[f]
                val second = baseBFS[s]

                val diff = abs(first.first - second.first) + abs(first.second - second.second)

                if (diff <= maxCheat) {
                    val newLength = baseBFS.size - (s - f) + diff
                    //println("Cheat at $first,$second is $newLength giving a saving of ${baseBFS.size - newLength}")
                    if (baseBFS.size - newLength >= considerSaving) {
                        cheats.add(Pair(first, second))
                    }
                }
            }
        }

        return cheats.size
    }

    fun part1(input: List<String>): Int {
        var startPos = Pair(0, 0)
        var endPos = Pair(0, 0)

        val height = input.size
        val width = input[0].length
        val gameBoard = Array(height) { CharArray(width) }

        for (row in 0..<height) {
            for (column in 0..<width) {
                when (input[row][column]) {
                    'S' -> startPos = Pair(row, column)
                    'E' -> endPos = Pair(row, column)
                    else -> gameBoard[row][column] = input[row][column]
                }
            }
        }

        // Get our entire path from start to finish
        val baseBFS = breadthFirstSearch(gameBoard, startPos, endPos)

        // A cheat is just a shortcut between two points on this path. This would
        // . remove the distance between the two points on the path
        // . add the Manhattan distance of the cheat
        val maxCheat = 2
        val considerSaving = 100
        return getNumberOfPossibleCheats(baseBFS, maxCheat, considerSaving)
    }

    fun part2(input: List<String>): Int {
        var startPos = Pair(0, 0)
        var endPos = Pair(0, 0)

        val height = input.size
        val width = input[0].length
        val gameBoard = Array(height) { CharArray(width) }

        for (row in 0..<height) {
            for (column in 0..<width) {
                when (input[row][column]) {
                    'S' -> startPos = Pair(row, column)
                    'E' -> endPos = Pair(row, column)
                    else -> gameBoard[row][column] = input[row][column]
                }
            }
        }

        // Get our base score by BFS without any cheating allowed
        val baseBFS = breadthFirstSearch(gameBoard, startPos, endPos)

        val maxCheat = 20
        val considerSaving = 100
        return getNumberOfPossibleCheats(baseBFS, maxCheat, considerSaving)
    }

    val input = readInput("Day20")
    var timeTaken = measureTime {
        part1(input).println()
    }
    println(timeTaken)

    timeTaken = measureTime {
        part2(input).println()
    }
    println(timeTaken)
}
