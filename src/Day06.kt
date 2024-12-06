import kotlin.time.measureTime

fun main() {

    fun getVisitedPositions(
        guardRow: Int,
        guardColumn: Int,
        guardRowIndex: Int,
        guardColumnIndex: Int,
        gameBoard: Array<CharArray>
    ): MutableSet<String> {
        // Now loop through
        var thisGuardRow = guardRow
        var thisGuardColumn = guardColumn
        var thisGuardRowIndex = guardRowIndex
        var thisGuardColumnIndex = guardColumnIndex
        var finished = false
        val visited = mutableSetOf<String>()
        while (!finished) {
            visited.add("$thisGuardRow,$thisGuardColumn")
            try {
                val offsetArray = arrayOf(-1, 0, 1, 0)
                val nextRow = thisGuardRow + offsetArray[thisGuardRowIndex]
                val nextColumn = thisGuardColumn + offsetArray[thisGuardColumnIndex]

                if (gameBoard[nextRow][nextColumn] == '#') {
                    thisGuardRowIndex++
                    if (thisGuardRowIndex > 3) thisGuardRowIndex = 0
                    thisGuardColumnIndex++
                    if (thisGuardColumnIndex > 3) thisGuardColumnIndex = 0
                } else {
                    thisGuardRow = nextRow
                    thisGuardColumn = nextColumn
                }
            } catch (e: Exception) {
                finished = true
            }
        }
        return visited
    }

    fun part1(input: List<String>): Int {
        val gameBoard = buildGameBoardFromInputByRow(input)

        var guardRow = 0
        var guardColumn = 0
        var guardRowIndex = 0
        var guardColumnIndex = 0
        // Find the position of the guard and their direction
        for (row in gameBoard.indices) {
            for (column in gameBoard[0].indices) {
                if (gameBoard[row][column] == '^') {
                    guardRow = row
                    guardColumn = column
                    guardRowIndex = 0
                    guardColumnIndex = 1
                    break
                }
                // Don't need to scan the rest, both test and actual input use caret
            }
            if (guardColumnIndex != 0)
                break
        }
        val visited = getVisitedPositions(guardRow, guardColumn, guardRowIndex, guardColumnIndex, gameBoard)

        return visited.size
    }

    fun part2(input: List<String>): Int {
        val gameBoard = buildGameBoardFromInputByRow(input)

        var guardStartRow = 0
        var guardStartColumn = 0
        var guardStartRowIndex = 99
        var guardStartColumnIndex = 99
        // Find the position of the guard and their direction
        for (row in gameBoard.indices) {
            for (column in gameBoard[0].indices) {
                if (gameBoard[row][column] == '^') {
                    guardStartRow = row
                    guardStartColumn = column
                    guardStartRowIndex = 0
                    guardStartColumnIndex = 1
                    break
                }
                // Don't need to scan the rest, both test and actual input use caret
            }
            if (guardStartColumnIndex != 99)
                break
        }

        // Optimisation, we only need to insert a blocker into the regular path
        // so first run the loop to get a list of positions to try
        val positionsToTry = getVisitedPositions(guardStartRow, guardStartColumn, guardStartRowIndex, guardStartColumnIndex, gameBoard)

        var numberWithLoops = 0
        for (pos in positionsToTry) {
            val thisGameBoard = gameBoard.deepClone()
            val split = pos.split(",")

            thisGameBoard[split[0].toInt()][split[1].toInt()] = '#'

            var guardRow = guardStartRow
            var guardColumn = guardStartColumn
            var guardRowIndex = guardStartRowIndex
            var guardColumnIndex = guardStartColumnIndex

            // Now loop through
            var finished = false
            val visited = mutableSetOf<String>()
            while (!finished)
            {
                val key = "$guardRow,$guardColumn,$guardRowIndex,$guardColumnIndex"
                if (visited.contains(key)) {
                    numberWithLoops++
                    finished = true
                }
                else
                {
                    visited.add(key)
                }

                try {
                    val offsetArray = arrayOf( -1, 0, 1, 0 )
                    val nextRow = guardRow + offsetArray[guardRowIndex]
                    val nextColumn = guardColumn + offsetArray[guardColumnIndex]

                    if (thisGameBoard[nextRow][nextColumn] == '#')
                    {
                        guardRowIndex++
                        if (guardRowIndex > 3) guardRowIndex = 0
                        guardColumnIndex++
                        if (guardColumnIndex > 3) guardColumnIndex = 0
                    }
                    else
                    {
                        guardRow = nextRow
                        guardColumn = nextColumn
                    }
                }
                catch(e: Exception)
                {
                    finished = true
                }
            }
        }

        return numberWithLoops
    }

    val input = readInput("Day06")
    var timeTaken = measureTime {
        part1(input).println()
    }
    println(timeTaken)

    timeTaken = measureTime {
        part2(input).println()
    }
    println(timeTaken)
}
