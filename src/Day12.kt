import java.util.*
import kotlin.time.measureTime

fun main() {

    fun part1(input: List<String>): Int {
        val gameBoard = buildGameBoardFromInputByRow(input)

        val visited = mutableSetOf<Pair<Int,Int>>()

        var totalPrice = 0

        // Start at the first character, flood fill from here using BFS
        // to identify all the elements within this area
        for (row in gameBoard.indices) {
            for (column in gameBoard.indices) {
                if (!visited.contains(Pair(row, column))) {
                    val thisArea = mutableSetOf<Pair<Int,Int>>()

                    val queue: Queue<Pair<Int, Int>> = LinkedList()

                    queue.add(Pair(row, column))
                    visited.add(Pair(row, column))
                    thisArea.add(Pair(row, column))
                    while (!queue.isEmpty()) {
                        val thisEntry = queue.remove()

                        for (i in arrayOf(Pair(-1,0),Pair(1,0), Pair(0,-1), Pair(0,1)))
                        {
                            try {
                                val dr = thisEntry.first + i.first
                                val dc = thisEntry.second + i.second
                                val dp = Pair(dr, dc)
                                if ((gameBoard[dr][dc] == gameBoard[row][column]) && !visited.contains(dp)) {
                                    visited.add(dp)
                                    thisArea.add(dp)
                                    queue.add(dp)
                                }
                            }
                            catch (ex: Exception) {
                                // Ignore this one
                            }
                        }
                    }

                    var perimeter = 0
                    for (p in thisArea) {
                        for (i in arrayOf(Pair(-1,0),Pair(1,0), Pair(0,-1), Pair(0,1))) {
                            val dr = p.first + i.first
                            val dc = p.second + i.second
                            if (!thisArea.contains(Pair(dr,dc))) {
                                perimeter++
                            }
                        }
                    }
                    totalPrice += perimeter * thisArea.size
                }
            }
        }
        return totalPrice
    }

    fun part2(input: List<String>): Int {
        val gameBoard = buildGameBoardFromInputByRow(input)

        val visited = mutableSetOf<Pair<Int,Int>>()

        var totalPrice = 0

        // Start at the first character, flood fill from here using BFS
        // to identify all the elements within this area
        for (row in gameBoard.indices) {
            for (column in gameBoard.indices) {
                if (!visited.contains(Pair(row, column))) {
                    val thisArea = mutableSetOf<Pair<Int,Int>>()

                    val queue: Queue<Pair<Int, Int>> = LinkedList()

                    queue.add(Pair(row, column))
                    visited.add(Pair(row, column))
                    thisArea.add(Pair(row, column))
                    while (!queue.isEmpty()) {
                        val thisEntry = queue.remove()

                        for (i in arrayOf(Pair(-1,0),Pair(1,0), Pair(0,-1), Pair(0,1)))
                        {
                            try {
                                val dr = thisEntry.first + i.first
                                val dc = thisEntry.second + i.second
                                val dp = Pair(dr, dc)
                                if ((gameBoard[dr][dc] == gameBoard[row][column]) && !visited.contains(dp)) {
                                    visited.add(dp)
                                    thisArea.add(dp)
                                    queue.add(dp)
                                }
                            }
                            catch (ex: Exception) {
                                // Ignore this one
                            }
                        }
                    }

                    // Calculate the number of sides...
                    var numberOfSides = 0
                    val edgesVisited = mutableSetOf<Pair<Pair<Int,Int>,Pair<Int,Int>>>()

                    // For each position, see whether it's got an edge, if it does then
                    // put an item outside that edge and push it in all relevant directions until
                    // we reach the end of this edge
                    for (p in thisArea) {
                        for (i in arrayOf(Pair(-1,0),Pair(1,0), Pair(0,-1), Pair(0,1))) {
                            val outsideItem = Pair(p.first + i.first, p.second + i.second)
                            if (!thisArea.contains(outsideItem) && !edgesVisited.contains(Pair(outsideItem,i))) {
                                // It's a new edge
                                numberOfSides++
                                edgesVisited.add(Pair(outsideItem, i))
                                val dirs = mutableListOf<Pair<Int,Int>>()
                                if (i.second == 0) {
                                    dirs.addAll(arrayOf(Pair(0,-1), Pair(0,1)))
                                }
                                else {
                                    dirs.addAll(arrayOf(Pair(1,0),Pair(-1,0)))
                                }
                                for (j in dirs) {
                                    var edge = outsideItem
                                    while (true)
                                    {
                                        val insideItem = Pair(edge.first - i.first, edge.second - i.second)
                                        if (thisArea.contains(insideItem) && !thisArea.contains(edge)) {
                                            edgesVisited.add(Pair(edge, i))
                                            edge = Pair(edge.first + j.first, edge.second + j.second)
                                        }
                                        else {
                                            break
                                        }
                                    }
                                }
                            }
                        }
                    }
                    println("Area ${gameBoard[row][column]} has $numberOfSides * ${thisArea.size} making a price ${numberOfSides * thisArea.size}")

                    totalPrice += numberOfSides * thisArea.size
                }
            }
        }
        return totalPrice
    }

    val input = readInput("Day12")
    var timeTaken = measureTime {
        part1(input).println()
    }
    println(timeTaken)

    timeTaken = measureTime {
        part2(input).println()
    }
    println(timeTaken)
}
