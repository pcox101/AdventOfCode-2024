import kotlin.math.abs

fun main() {

    fun getxmases(gameBoard: Array<CharArray>, xoffset: Int, yoffset: Int): Int {
        // Definitely a more efficient way of doing this, but
        // search for x then check in offset direction for m then a then s. If found add 1
        var counter = 0;

        for (x in 0..<gameBoard[0].size)
            for (y in 0..<gameBoard.size) {
                var foundXMas = true;
                try {
                    var currentxOffset = x;
                    var currentyOffset = y;
                    for (char in arrayOf('X', 'M', 'A', 'S')) {
                        if (gameBoard[currentxOffset][currentyOffset] != char) {
                            foundXMas = false;
                            break;
                        }
                        currentyOffset+=yoffset;
                        currentxOffset+=xoffset;
                    }
                } catch (e: Exception) {
                    foundXMas = false;
                }
                if (foundXMas)
                    counter++;
            }
        return counter;
    }

    fun getmas(gameBoard: Array<CharArray>, xoffset: Int, yoffset: Int): Set<String> {
        var counter = 0;
        var found = mutableSetOf<String>()

        for (x in 0..<gameBoard[0].size)
            for (y in 0..<gameBoard.size) {
                var foundMas = true;
                try {
                    var currentxOffset = x;
                    var currentyOffset = y;
                    for (char in arrayOf('M', 'A', 'S')) {
                        if (gameBoard[currentxOffset][currentyOffset] != char) {
                            foundMas = false;
                            break;
                        }
                        currentyOffset+=yoffset;
                        currentxOffset+=xoffset;
                    }
                } catch (e: Exception) {
                    foundMas = false;
                }
                if (foundMas) {
                    val tx = x + xoffset;
                    val ty = y + yoffset;
                    found.add("$tx,$ty")
                }
            }
        return found;
    }

    fun part1(input: List<String>): Int {
        val gameboard = buildGameBoardFromInput(input)

        var xmases = 0;

        // Loop through the 8 possible directions
        for (x in -1..1)
            for (y in -1..1)
                if ((x != 0) || (y != 0))
                    xmases += getxmases(gameboard, x, y)

        return xmases;
    }

    fun part2(input: List<String>): Int {
        val gameboard = buildGameBoardFromInput(input)

        var xmases = 0;

        val sets = mutableListOf<Set<String>>()

        // We only need to loop through 4 directions this time, looking for mas
        // But we need to keep a list of all of the coordinates of all of the A we found
        for (x in -1..1)
            for (y in -1..1)
                if ((x != 0) && (y != 0))
                    sets.add(getmas(gameboard, x, y))

        // Now see how many are in at least 1 set
        for (x in 0..<gameboard[0].size)
            for (y in 0..<gameboard.size) {
                var counter = 0;
                for (n in 0..<sets.size) {
                    if (sets[n].contains("$x,$y")) counter++
                }

                if (counter > 1) xmases++;
            }

        return xmases;
    }

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}
