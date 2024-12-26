import kotlin.time.measureTime

fun main() {

    fun part1(input: List<String>): Int {
        val locks = mutableListOf<Array<Int>>()
        val keys = mutableListOf<Array<Int>>()

        var lockIndex = 0
        var keyIndex = 0
        var inLock = false
        var firstLine = true
        for (i in input) {
            if (i == "") {

                // Subtract 1 from the keys as we've included the bottom row
                if (!inLock) {
                    for (j in 0..4) {
                        keys[keyIndex][j]--
                    }
                }

                if (inLock) lockIndex++
                else keyIndex++
                firstLine = true
                continue
            }

            if (firstLine) {
                if (i == "#####") {
                    inLock = true
                    locks.add(arrayOf(0,0,0,0,0))
                }
                else {
                    inLock = false
                    keys.add(arrayOf(0,0,0,0,0))
                }
                firstLine = false
                continue
            }

            for (j in i.indices) {
                if (i[j] == '#') {
                    if (inLock) {
                        locks[lockIndex][j] += 1
                    } else {
                        keys[keyIndex][j] += 1
                    }
                }
            }
        }

        // And the last one
        if (!inLock) {
            for (j in 0..4) {
                keys[keyIndex][j]--
            }
        }

        // Now you can try every key with every lock
        var counter = 0

        for (k in keys) {
            for (l in locks) {
                var overlaps = false
                for (i in 0..4) {
                    if (l[i] + k[i] > 5)
                    {
                        //println("Lock $l and key $k overlap in column $i")
                        overlaps = true
                        break
                    }
                }
                if (!overlaps) {
                    //println("Lock $l and key $k do not overlap")
                    counter++
                }
            }
        }

        return counter
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val input = readInput("Day25")
    var timeTaken = measureTime {
        part1(input).println()
    }
    println(timeTaken)

    timeTaken = measureTime {
        part2(input).println()
    }
    println(timeTaken)
}
