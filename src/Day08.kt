import kotlin.time.measureTime

fun main() {

    fun makeFrequencyMap(input: List<String>): MutableMap<String, MutableList<Pair<Int, Int>>> {
        val frequencies = mutableMapOf<String, MutableList<Pair<Int, Int>>>()
        for ((row, i) in input.withIndex()) {
            for (j in i.indices) {
                if (i[j] != '.') {
                    val s = i[j].toString()
                    var l = frequencies[s]
                    if (l != null) {
                        l.add(Pair(row, j))
                    } else {
                        l = mutableListOf()
                        l.add(Pair(row, j))
                        frequencies[s] = l
                    }
                }
            }
        }
        return frequencies
    }

    fun part1(input: List<String>): Int {
        val rowSize = input.size
        val columnSize = input[0].length

        val frequencies = makeFrequencyMap(input)

        val antiNodes = hashSetOf<Pair<Int, Int>>()
        for (freq in frequencies)
        {
            val antennas = freq.value
            for (i in antennas.indices)
                for (j in i+1..<antennas.size) {
                    val a1 = antennas[i]
                    val a2 = antennas[j]
                    val rowDiff = a1.first - a2.first
                    val colDiff = a1.second - a2.second

                    val p1 = Pair(a1.first + rowDiff, a1.second + colDiff)
                    val p2 = Pair(a2.first - rowDiff, a2.second - colDiff)

                    if ((p1.first >= 0) && (p1.first < rowSize)
                        && (p1.second >= 0) && (p1.second < columnSize))
                    {
                        antiNodes.add(p1)
                    }
                    if ((p2.first >= 0) && (p2.first < rowSize)
                        && (p2.second >= 0) && (p2.second < columnSize))
                    {
                        antiNodes.add(p2)
                    }
                }
        }

        return antiNodes.size
    }

    fun part2(input: List<String>): Int {
        val rowSize = input.size
        val columnSize = input[0].length

        val frequencies = makeFrequencyMap(input)

        val antiNodes = hashSetOf<Pair<Int, Int>>()
        for (freq in frequencies)
        {
            val antennas = freq.value
            for (i in antennas.indices)
                for (j in i+1..<antennas.size) {
                    val a1 = antennas[i]
                    val a2 = antennas[j]
                    val rowDiff = a1.first - a2.first
                    val colDiff = a1.second - a2.second

                    // Loop in each direction until we go off the map
                    var thisRowDiff = rowDiff
                    var thisColDiff = colDiff
                    while (true) {
                        val p1 = Pair(a1.first + thisRowDiff, a1.second + thisColDiff)

                        if ((p1.first >= 0) && (p1.first < rowSize)
                            && (p1.second >= 0) && (p1.second < columnSize))
                        {
                            antiNodes.add(p1)
                            thisRowDiff += rowDiff
                            thisColDiff += colDiff
                        }
                        else
                            break
                    }

                    thisRowDiff = rowDiff
                    thisColDiff = colDiff
                    while (true)
                    {
                        val p2 = Pair(a2.first - thisRowDiff, a2.second - thisColDiff)
                        if ((p2.first >= 0) && (p2.first < rowSize)
                            && (p2.second >= 0) && (p2.second < columnSize)
                        ) {
                            antiNodes.add(p2)
                            thisRowDiff += rowDiff
                            thisColDiff += colDiff
                        }
                        else
                            break
                    }
                }
            // Now add all the antennas themselves (but only if there is more
            // than one of them
            if (antennas.size > 1) {
                antiNodes.addAll(antennas)
            }
        }

        return antiNodes.size
    }

    val input = readInput("Day08")
    var timeTaken = measureTime {
        part1(input).println()
    }
    println(timeTaken)

    timeTaken = measureTime {
        part2(input).println()
    }
    println(timeTaken)
}
