import java.util.*
import kotlin.time.measureTime

fun main() {

    fun calculateTrailHeadScore(trailHeadRow: Int, trailHeadColumn: Int, gameBoard: Array<CharArray>): Int
    {
        var destinations = mutableSetOf<Pair<Int, Int>>()

        var queue: Queue<Triple<Int, Int, Int>> = LinkedList()

        queue.add(Triple(trailHeadRow, trailHeadColumn,0))

        while (!queue.isEmpty()) {
            val t = queue.remove()

            if (gameBoard[t.first][t.second] == '9') {
                destinations.add(Pair(t.first,t.second))
                continue
            }

            for (rowOffset in -1..1) {
                for (columnOffset in -1..1) {
                    if ((rowOffset == 0) || (columnOffset == 0)) {
                        val newRow = t.first + rowOffset
                        val newColumn = t.second + columnOffset
                        try {
                            if (gameBoard[newRow][newColumn].toString().toInt() == t.third + 1) {
                                queue.add(Triple(newRow, newColumn, t.third + 1))
                            }
                        } catch (e: Exception) {
                            continue
                        }
                    }
                }
            }
        }

        return destinations.size
    }

    fun part1(input: List<String>): Int {
        var gameBoard = buildGameBoardFromInputByRow(input)

        var totalScore = 0
        for (row in 0..<gameBoard.size)
        {
            for (column in 0..<gameBoard[row].size)
            {
                if (gameBoard[row][column] == '0')
                    totalScore += calculateTrailHeadScore(row, column, gameBoard)
            }
        }
        return totalScore
    }

    fun calculateTrailHeadRating(trailHeadRow: Int, trailHeadColumn: Int, gameBoard: Array<CharArray>): Int
    {
        var destinations = mutableSetOf<String>()

        var queue: Queue<Triple<String, Int, String>> = LinkedList()

        queue.add(Triple("$trailHeadRow,$trailHeadColumn",0,""))

        while (!queue.isEmpty()) {
            val t = queue.remove()
            val s = t.first.split(",")
            val row = s[0].toInt()
            val column = s[1].toInt()

            val newTrail = t.third + "$row,$column,"

            if (gameBoard[row][column] == '9') {
                destinations.add(newTrail)
                continue
            }

            for (rowOffset in -1..1) {
                for (columnOffset in -1..1) {
                    if ((rowOffset == 0) || (columnOffset == 0)) {
                        val newRow = row + rowOffset
                        val newColumn = column + columnOffset
                        try {
                            if (gameBoard[newRow][newColumn].toString().toInt() == t.second + 1) {
                                queue.add(Triple("$newRow,$newColumn", t.second + 1, newTrail))
                            }
                        } catch (e: Exception) {
                            continue
                        }
                    }
                }
            }
        }

        return destinations.size
    }

    fun part2(input: List<String>): Int {
        var gameBoard = buildGameBoardFromInputByRow(input)

        var totalScore = 0
        for (row in 0..<gameBoard.size)
        {
            for (column in 0..<gameBoard[row].size)
            {
                if (gameBoard[row][column] == '0')
                    totalScore += calculateTrailHeadRating(row, column, gameBoard)
            }
        }
        return totalScore
    }

    val input = readInput("Day10")
    var timeTaken = measureTime {
        part1(input).println()
    }
    println(timeTaken)

    timeTaken = measureTime {
        part2(input).println()
    }
    println(timeTaken)
}
