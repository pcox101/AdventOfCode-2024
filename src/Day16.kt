import java.util.*
import kotlin.time.measureTime

fun main() {

    data class QueueEntry(val position: Pair<Int,Int>, val score: Int, val direction: Pair<Int,Int>, val visited: Set<Pair<Int,Int>>, val rotated: Boolean)

    fun part1And2(input: List<String>): Pair<Int,Int> {
        val height = input.size
        val width = input[0].length

        var startPoint = Pair(0,0)
        var endPoint = Pair(0,0)

        val gameBoard = Array(height) { CharArray(width) }

        for (row in input.indices) {
            for (column in input[row].indices) {
                if (input[row][column] == 'S') {
                    startPoint = Pair(row, column)
                    gameBoard[row][column] = '.'
                }
                else if (input[row][column] == 'E') {
                    endPoint = Pair(row, column)
                    gameBoard[row][column] = '.'
                }
                else {
                    gameBoard[row][column] = input[row][column]
                }
            }
        }
        
        val lowestScoreMap = mutableMapOf<Pair<Int,Int>,Int>()
        val comfortableSeats = mutableSetOf<Pair<Int,Int>>()

        var lowestScore = Int.MAX_VALUE

        // Dijkstra through the game board
        val q = PriorityQueue<QueueEntry> { p1, p2 -> (p1.score - p2.score) }

        q.add(QueueEntry(startPoint, 0, Pair(0,1), mutableSetOf(), false))

        while (q.isNotEmpty()) {
            val entry = q.remove()

            if (entry.score > lowestScore) continue

            if (entry != null) {
                //println("Current: ${entry.score} - ${q.size}")
                if (entry.position == endPoint)
                {
                    // We have now reached an end point.
                    comfortableSeats.addAll(entry.visited)
                    lowestScore = entry.score
                    continue
                }

                // Is this a node? In which case check whether we've been at this node already
                // with a lower score
                if (entry.rotated)
                {
                    val currentLowestScoreAtThisNode = lowestScoreMap[entry.position]
                    if (currentLowestScoreAtThisNode != null)
                    {
                        if (currentLowestScoreAtThisNode < entry.score) {
                            // terminate this path
                            continue
                        }
                        else
                        {
                            lowestScoreMap[entry.position] = entry.score
                        }
                    }
                    else {
                        lowestScoreMap[entry.position] = entry.score
                    }
                }

                val newVisited = entry.visited.toMutableSet()
                newVisited.add(entry.position)

                // Options are...
                // Move forward (if possible) - 1 point
                val forward = Pair(entry.position.first + entry.direction.first, entry.position.second + entry.direction.second)
                if ((gameBoard[forward.first][forward.second] != '#') && (!entry.visited.contains(forward))) {
                    q.add(QueueEntry(forward, entry.score + 1, entry.direction, newVisited.toSet(), false))
                }
                // No point rotating more than once on any spot
                if (!entry.rotated) {
                    // Rotate clockwise - 1000 points
                    val cwDirection = when (entry.direction) {
                        Pair(0, 1) -> Pair(1, 0)
                        Pair(1, 0) -> Pair(0, -1)
                        Pair(0, -1) -> Pair(-1, 0)
                        Pair(-1, 0) -> Pair(0, 1)
                        else -> Pair(0, 100)
                    }
                    // Avoid turning into a wall
                    if (gameBoard[entry.position.first + cwDirection.first][entry.position.second + cwDirection.second] != '#')
                        q.add(QueueEntry(entry.position, entry.score + 1000, cwDirection, newVisited.toSet(), true))

                    // Rotate anti-clockwise - 1000 points
                    val acwDirection = when (entry.direction) {
                        Pair(0, 1) -> Pair(-1, 0)
                        Pair(1, 0) -> Pair(0, 1)
                        Pair(0, -1) -> Pair(1, 0)
                        Pair(-1, 0) -> Pair(0, -1)
                        else -> Pair(0, 100)
                    }
                    // Avoid turning into a wall
                    if (gameBoard[entry.position.first + acwDirection.first][entry.position.second + acwDirection.second] != '#')
                        q.add(QueueEntry(entry.position, entry.score + 1000, acwDirection, newVisited.toSet(), true))
                }
            }
        }
        return Pair(lowestScore, comfortableSeats.size + 1)
    }

    val input = readInput("Day16")
    val timeTaken = measureTime {
        val results = part1And2(input)
        results.first.println()
        results.second.println()
    }
    println(timeTaken)
}
