fun main() {

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

        // Now loop through
        var finished = false
        val visited = mutableSetOf<String>()
        while (!finished)
        {
            visited.add("$guardRow,$guardColumn")
            try {
                val offsetArray = arrayOf( -1, 0, 1, 0 )
                val nextRow = guardRow + offsetArray[guardRowIndex]
                val nextColumn = guardColumn + offsetArray[guardColumnIndex]

                if (gameBoard[nextRow][nextColumn] == '#')
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
                finished = true;
            }
        }
        return visited.size
    }

    fun part2(input: List<String>): Int {
        val gameBoard = buildGameBoardFromInputByRow(input)

        var guardStartRow = 0
        var guardStartColumn = 0
        var guardStartRowIndex = 0
        var guardStartColumnIndex = 0
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
            if (guardStartColumnIndex != 0)
                break
        }

        // We need to see how many times we can insert a loop, so
        // brute force this by trying a block in every position then
        // running the game loop to see if it loops
        var numberWithLoops = 0
        for (tryRow in gameBoard.indices)
            for (tryColumn in gameBoard[tryRow].indices) {
                var thisGameBoard = gameBoard.deepClone()
                thisGameBoard[tryRow][tryColumn] = '#'

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
                        var nextRow = guardRow + offsetArray[guardRowIndex]
                        var nextColumn = guardColumn + offsetArray[guardColumnIndex]

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
                        finished = true;
                    }
                }
            }
        return numberWithLoops
    }


    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}
