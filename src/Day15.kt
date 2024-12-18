import java.util.*
import kotlin.time.measureTime

fun main() {

    fun outputGameBoard(move: String, gameBoard: Map<Pair<Int,Int>, String>, height: Int, width: Int, robotPos: Pair<Int,Int>) {
        println()
        println("GameBoard after move $move:")
        for (row in 0..<height) {
            for (column in 0..<width) {
                if (robotPos == Pair(row, column)) print("@")
                else {
                    if (gameBoard[Pair(row,column)] == null) print(".")
                    else print(gameBoard[Pair(row,column)])
                }
            }
            println()
        }
    }

    fun part1(input: List<String>): Int {
        val gameBoard = mutableMapOf<Pair<Int,Int>,String>()
        val moves = StringBuilder()
        var robotPos = Pair(0,0)

        var height = 0
        var width = 0
        var inGameBoard = true
        for ((row, s) in input.withIndex())
        {
            if (s == ""){
                height = row
                inGameBoard = false
            }

            if (inGameBoard)
            {
                width = s.length
                for (col in s.indices) {
                    val ch = s[col].toString()
                    if (ch == "@") {
                        robotPos = Pair(row, col)
                    }
                    else if (ch != ".") {
                        gameBoard[Pair(row, col)] = ch
                    }
                }
            }
            else
            {
                moves.append(s)
            }
        }

        // Now play through the moves to move the robot around
        for (ch in moves) {
            var delta = Pair(0,0)
            when (ch.toString()) {
                "<"-> delta = Pair(0,-1)
                "^"-> delta = Pair(-1,0)
                ">"-> delta = Pair(0, 1)
                "v"-> delta = Pair(1, 0)
            }

            var movePos = Pair(robotPos.first + delta.first, robotPos.second + delta.second)
            while ((gameBoard[movePos] != "#") && (gameBoard[movePos] != null)) {
                movePos = Pair(movePos.first + delta.first, movePos.second + delta.second)
            }
            if (gameBoard[movePos] == null) {
                movePos = Pair(movePos.first - delta.first, movePos.second - delta.second)
                while (movePos != robotPos) {
                    gameBoard.remove(movePos)
                    gameBoard[Pair(movePos.first + delta.first, movePos.second + delta.second)] = "O"
                    movePos = Pair(movePos.first - delta.first, movePos.second - delta.second)
                }
                robotPos = Pair(robotPos.first + delta.first, robotPos.second + delta.second)
            }
            outputGameBoard(ch.toString(), gameBoard, height, width, robotPos)
        }

        // We've finished all the moves
        // Calculate the "GPS"
        var gps = 0
        for (rock in gameBoard) {
            if (rock.value == "O") gps += (100 * (rock.key.first) + rock.key.second)
        }

        return gps
    }

    fun part2(input: List<String>): Int {
        // The game board is twice as big...
        val gameBoard = mutableMapOf<Pair<Int,Int>,String>()
        val moves = StringBuilder()
        var robotPos = Pair(0,0)

        var height = 0
        var width = 0
        var inGameBoard = true
        for ((row, s) in input.withIndex())
        {
            if (s == ""){
                height = row
                inGameBoard = false
            }

            if (inGameBoard)
            {
                width = s.length * 2
                for (col in s.indices) {
                    val ch = s[col].toString()
                    when (ch) {
                        "@" -> {
                            robotPos = Pair(row, col * 2)
                        }

                        "#" -> {
                            gameBoard[Pair(row, col * 2)] = "#"
                            gameBoard[Pair(row, col * 2 + 1)] = "#"
                        }

                        "O" -> {
                            gameBoard[Pair(row, col * 2)] = "["
                            gameBoard[Pair(row, col * 2 + 1)] = "]"
                        }
                    }
                }
            }
            else
            {
                moves.append(s)
            }
        }

        // Now play through the moves to move the robot around
        for (ch in moves) {
            var delta = Pair(0,0)
            when (ch.toString()) {
                "<"-> delta = Pair(0,-1)
                "^"-> delta = Pair(-1,0)
                ">"-> delta = Pair(0, 1)
                "v"-> delta = Pair(1, 0)
            }

            // Horizontal moves remain the same
            if (delta.first == 0) {
                var movePos = Pair(robotPos.first, robotPos.second + delta.second)
                while ((gameBoard[movePos] != "#") && (gameBoard[movePos] != null)) {
                    movePos = Pair(movePos.first, movePos.second + delta.second)
                }
                if (gameBoard[movePos] == null) {
                    movePos = Pair(movePos.first, movePos.second - delta.second)
                    while (movePos != robotPos) {
                        val v = gameBoard.remove(movePos)
                        if (v != null) gameBoard[Pair(movePos.first, movePos.second + delta.second)] = v
                        movePos = Pair(movePos.first, movePos.second - delta.second)
                    }
                    robotPos = Pair(robotPos.first, robotPos.second + delta.second)
                }
            }
            else {
                // Build up our list of positions to move. We do this by starting with the robot
                // position and checking up/down. If that is a rock then add both of the rock to our queue of things
                // to check next time
                val potentialMovingThings = mutableMapOf<Pair<Int,Int>,String?>()
                val queue: Queue<Pair<Int,Int>> = LinkedList()
                queue.add(robotPos)
                var anythingMoved = true
                while (!queue.isEmpty())
                {
                    val movePos = queue.remove()
                    if (movePos != null) {
                        potentialMovingThings[movePos] = gameBoard[movePos]
                        // Look at what is above this position
                        val nextPos = Pair(movePos.first + delta.first, movePos.second + delta.second)
                        if (gameBoard[nextPos] == "#") {
                            anythingMoved = false
                            break
                        }
                        else if (gameBoard[nextPos] == "[") {
                            queue.add(nextPos)
                            queue.add(Pair(nextPos.first, nextPos.second + 1))
                        }
                        else if (gameBoard[nextPos] == "]") {
                            queue.add(nextPos)
                            queue.add(Pair(nextPos.first, nextPos.second - 1))
                        }
                    }
                }

                if (anythingMoved)
                {
                    for (mover in potentialMovingThings) {
                        gameBoard.remove(mover.key)
                    }
                    for (mover in potentialMovingThings) {
                        if (mover.value == null) {
                            robotPos = Pair(robotPos.first + delta.first, robotPos.second + delta.second)
                        }
                        else {
                            gameBoard[Pair(mover.key.first + delta.first, mover.key.second + delta.second)] = mover.value!!
                        }
                    }
                }
            }
            outputGameBoard(ch.toString(), gameBoard, height, width, robotPos)
        }

        // We've finished all the moves
        // Calculate the "GPS"
        var gps = 0
        for (rock in gameBoard) {
            if (rock.value == "[") gps += (100 * (rock.key.first) + rock.key.second)
        }

        return gps
    }

    val input = readInput("Day15")
    var timeTaken = measureTime {
        part1(input).println()
    }
    println(timeTaken)

    timeTaken = measureTime {
        part2(input).println()
    }
    println(timeTaken)
}
