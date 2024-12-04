fun main() {

    fun getXmases(gameBoard: Array<CharArray>, xOffset: Int, yOffset: Int): Int {
        // Definitely a more efficient way of doing this, but
        // search for x then check in offset direction for m then a then s. If found add 1
        var counter = 0

        for (x in 0..<gameBoard[0].size)
            for (y in gameBoard.indices) {
                var foundXMas = true
                try {
                    var currentXOffset = x
                    var currentYOffset = y
                    for (char in arrayOf('X', 'M', 'A', 'S')) {
                        if (gameBoard[currentXOffset][currentYOffset] != char) {
                            foundXMas = false
                            break
                        }
                        currentXOffset += xOffset
                        currentYOffset += yOffset
                    }
                } catch (e: Exception) {
                    foundXMas = false
                }
                if (foundXMas)
                    counter++
            }
        return counter
    }

    fun getMas(gameBoard: Array<CharArray>, xOffset: Int, yOffset: Int): Set<String> {
        val found = mutableSetOf<String>()

        for (x in 0..<gameBoard[0].size)
            for (y in gameBoard.indices) {
                var foundMas = true
                try {
                    var currentXOffset = x
                    var currentYOffset = y
                    for (char in arrayOf('M', 'A', 'S')) {
                        if (gameBoard[currentXOffset][currentYOffset] != char) {
                            foundMas = false
                            break
                        }
                        currentYOffset += yOffset
                        currentXOffset += xOffset
                    }
                } catch (e: Exception) {
                    foundMas = false
                }
                if (foundMas) {
                    val tx = x + xOffset
                    val ty = y + yOffset
                    found.add("$tx,$ty")
                }
            }
        return found
    }

    fun part1(input: List<String>): Int {
        val gameBoard = buildGameBoardFromInput(input)

        var xmases = 0

        // Loop through the 8 possible directions
        for (x in -1..1)
            for (y in -1..1)
                if ((x != 0) || (y != 0))
                    xmases += getXmases(gameBoard, x, y)

        return xmases
    }

    fun part2(input: List<String>): Int {
        val gameBoard = buildGameBoardFromInput(input)

        var xmases = 0

        val sets = mutableListOf<Set<String>>()

        // We only need to loop through 4 directions this time, looking for mas
        // But we need to keep a list of the coordinates of the A we found
        for (x in -1..1)
            for (y in -1..1)
                if ((x != 0) && (y != 0))
                    sets.add(getMas(gameBoard, x, y))

        // Now see how many are in at least 1 set
        for (x in 0..<gameBoard[0].size)
            for (y in gameBoard.indices) {
                var counter = 0
                for (n in 0..<sets.size) {
                    if (sets[n].contains("$x,$y")) counter++
                }

                if (counter > 1) xmases++
            }

        return xmases
    }

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}
