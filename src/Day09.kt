import kotlin.time.measureTime

fun main() {

    fun processLineIntoFileSystem2(input: List<String>): MutableList<Pair<Int,Int?>> {
        val fileSystem = mutableListOf<Pair<Int,Int?>>()

        var inFile = true
        var fileNumber = 0
        for (i in input[0].map { it.toString().toInt() }) {
            if (inFile) {
                if (i > 0) fileSystem.add(Pair(i, fileNumber))
            } else {
                if (i > 0) fileSystem.add(Pair(i, null))
            }
            if (inFile) fileNumber++
            inFile = !inFile

        }
        return fileSystem
    }

    fun calculateChecksum(fileSystem: MutableList<Pair<Int, Int?>>): Long {
        var checksum = 0.toLong()
        var counter = 0
        for (i in fileSystem.indices) {
            val v = fileSystem[i]
            for (j in 0..<v.first) {
                if (v.second != null) {
                    checksum += v.second!! * counter
                }
                counter++
            }
        }
        return checksum
    }

    fun part1(input: List<String>): Long {
        val fileSystem = processLineIntoFileSystem2(input)

        var target = 0
        var source = fileSystem.size - 1
        // Work from the back, filling nulls from the front
        while (source > target + 1) {
            if (fileSystem[source].second != null) {
                while (fileSystem[target].second != null) {
                    target++
                }

                // Copy the whole source to target
                if (fileSystem[target].first >= fileSystem[source].first) {
                    val remainder = fileSystem[target].first - fileSystem[source].first
                    fileSystem[target] = Pair(fileSystem[source].first, fileSystem[source].second)
                    fileSystem[source] = Pair(fileSystem[source].first, null)
                    if (remainder > 0) {
                        fileSystem.add(target + 1, Pair(remainder, null))
                        source++
                    }
                }
                else {
                    val remainder = fileSystem[source].first - fileSystem[target].first
                    fileSystem[target] = Pair(fileSystem[target].first, fileSystem[source].second)
                    fileSystem[source] = Pair(remainder, fileSystem[source].second)

                    // Still more to do in the source
                    source++
                }
            }
            source--
        }

        return calculateChecksum(fileSystem)
    }

    fun part2(input: List<String>): Long {
        val fileSystem = processLineIntoFileSystem2(input)

        // Work from the back moving items into the first available space that fits it
        var source = fileSystem.size - 1
        while (source > 0)
        {
            if (fileSystem[source].second != null) {
                // Find a gap that fits it
                for (target in 0 ..< source)
                {
                    if ((fileSystem[target].second == null)
                        && (fileSystem[target].first >= fileSystem[source].first))
                    {
                        // It fits
                        val remainingBlank = fileSystem[target].first - fileSystem[source].first
                        val newFirst = Pair(fileSystem[target].first - remainingBlank, fileSystem[source].second)
                        fileSystem[target] = newFirst
                        if (remainingBlank > 0) {
                            fileSystem.add(target + 1, Pair(remainingBlank, null))
                            source++
                        }
                        fileSystem[source] = Pair(fileSystem[source].first, null)
                        break
                    }
                }
            }
            source--
        }

        return calculateChecksum(fileSystem)
    }

    val input = readInput("Day09")
    var timeTaken = measureTime {
        part1(input).println()
    }
    println(timeTaken)

    timeTaken = measureTime {
        part2(input).println()
    }
    println(timeTaken)
}
