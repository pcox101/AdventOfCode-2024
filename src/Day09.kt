import kotlin.time.measureTime

fun main() {

    fun processLineIntoFileSystem(input: List<String>): MutableList<Int?> {
        val fileSystem = mutableListOf<Int?>()

        var inFile = true
        var fileNumber = 0
        for (i in input[0].map { it.toString().toInt() }) {
            for (j in 0..<i) {
                if (inFile) {
                    fileSystem.add(fileNumber)
                } else {
                    fileSystem.add(null)
                }
            }
            if (inFile) fileNumber++
            inFile = !inFile

        }
        return fileSystem
    }

    fun part1(input: List<String>): Long {
        val fileSystem = processLineIntoFileSystem(input)

        var currentOffset = 0
        // Work from the back, filling nulls from the front
        for(i in fileSystem.size - 1 downTo 0) {
            if (fileSystem[i] != null)
            {
                while (fileSystem[currentOffset] != null)
                {
                    currentOffset++;
                }
                if (i < currentOffset) break
                fileSystem[currentOffset] = fileSystem[i]
                fileSystem[i] = null
            }
        }

        var checksum = 0.toLong()
        for(i in fileSystem.indices)
        {
            val v = fileSystem[i]
            if (v != null)
            {
                checksum += i * v
            }
        }

        return checksum
    }

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
