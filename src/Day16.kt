import java.util.*
import kotlin.time.measureTime

fun main() {

    data class QueueEntry(val position: Pair<Int,Int>, val score: Int, val direction: Pair<Int,Int>, val visited: Set<Pair<Int,Int>>, val rotated: Boolean)

    fun part1(input: List<String>): Int {
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

        // Dijkstra through the game board
        val q = PriorityQueue<QueueEntry> { p1, p2 -> (p1.score - p2.score) }

        q.add(QueueEntry(startPoint, 0, Pair(0,1), mutableSetOf<Pair<Int,Int>>(), false))

        while (q.isNotEmpty()) {
            val entry = q.remove()
            if (entry != null) {
                //println("Current: ${entry.score} - ${q.size}")
                if (entry.position == endPoint)
                {
                    return entry.score
                }

                // Have we already been here with a lower score?
                if (!entry.rotated) {
                    val lowestScore = lowestScoreMap[entry.position]
                    if (lowestScore != null) {
                        if (lowestScore < entry.score) {
                            continue
                        }
                    } else {
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
                    q.add(QueueEntry(entry.position, entry.score + 1000, cwDirection, newVisited.toSet(), true))
                    // Rotate anti-clockwise - 1000 points
                    val acwDirection = when (entry.direction) {
                        Pair(0, 1) -> Pair(-1, 0)
                        Pair(1, 0) -> Pair(0, 1)
                        Pair(0, -1) -> Pair(1, 0)
                        Pair(-1, 0) -> Pair(0, -1)
                        else -> Pair(0, 100)
                    }
                    q.add(QueueEntry(entry.position, entry.score + 1000, acwDirection, newVisited.toSet(), true))
                }
            }
        }
        return 0
    }

    fun part2(input: List<String>, knownShortestRoute: Int): Int {
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

        // Dijkstra through the game board
        val q = PriorityQueue<QueueEntry> { p1, p2 -> (p1.score - p2.score) }

        q.add(QueueEntry(startPoint, 0, Pair(0,1), mutableSetOf<Pair<Int,Int>>(), false))
        while (q.isNotEmpty()) {
            val entry = q.remove()
            println("${q.size} - ${entry.score}")

            // Terminate this route if it's longer than the known shortest
            if (entry.score > knownShortestRoute) continue

            // Have we reached an endpoint?
            if (entry.position == endPoint)
            {
                // We have now reached an end point. This will probably be the shortest route as otherwise we'd
                // have terminated it already, but check anyway...
                if (entry.score == knownShortestRoute)
                {
                    comfortableSeats.addAll(entry.visited)
                }
                continue
            }

            // Is this a node, in which case check whether we've been at this node already
            // with a lower score (assume that we'll be rotating here anyway, which we might
            // not be, but that shouldn't be too bad on the optimisation
            if (entry.rotated)
            {
                val currentLowestScoreAtThisNode = lowestScoreMap[entry.position]
                if (currentLowestScoreAtThisNode != null)
                {
                    if (currentLowestScoreAtThisNode < entry.score + 1000) {
                        // terminate this path
                        continue
                    }
                    else
                    {
                        lowestScoreMap[entry.position] = entry.score + 1000
                    }
                }
                else {
                    lowestScoreMap[entry.position] = entry.score + 1000
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
                // Don't bother adding if we're turning into a wall
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
                if (gameBoard[entry.position.first + acwDirection.first][entry.position.second + acwDirection.second] != '#')
                    q.add(QueueEntry(entry.position, entry.score + 1000, acwDirection, newVisited.toSet(), true))
            }
        }
        return comfortableSeats.size + 1
    }

    val input = readInput("Day16")
    var shortestRoute = 0
    var timeTaken = measureTime {
        shortestRoute = part1(input)
        shortestRoute.println()
    }
    println(timeTaken)

    timeTaken = measureTime {
        part2(input, shortestRoute).println()
    }
    println(timeTaken)
}
