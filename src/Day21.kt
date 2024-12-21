import kotlin.math.abs
import kotlin.time.measureTime

class Holder(private val totalDepth: Int)
{
    private val numberMap = mapOf(Pair('7',Pair(0,0)),Pair('8',Pair(0,1)),Pair('9',Pair(0,2)),
                                  Pair('4',Pair(1,0)),Pair('5',Pair(1,1)),Pair('6',Pair(1,2)),
                                  Pair('1',Pair(2,0)),Pair('2',Pair(2,1)),Pair('3',Pair(2,2)),
                                  Pair('0',Pair(3,1)),Pair('A',Pair(3,2)))

    private val arrowMap = mapOf(                     Pair('^',Pair(0,1)),Pair('A',Pair(0,2)),
                                  Pair('<',Pair(1,0)),Pair('v',Pair(1,1)),Pair('>',Pair(1,2)))



    private val memo = mutableMapOf<Triple<Char, Char, Int>,Long>()

    private fun getScoreForStep(prev: Char,
                                thisChar: Char,
                                depth: Int
    ): Long {

        val cache = memo[Triple(prev, thisChar, depth)]
        if (cache != null)
            return cache

        var thisMap = arrowMap
        if (depth == 0)
            thisMap = numberMap

        val startPos = thisMap[prev]!!
        val endPos = thisMap[thisChar]!!

        val rows = endPos.first - startPos.first
        val cols = endPos.second - startPos.second

        val sb = StringBuilder()

        // We can't just go for the most efficient way, in case it puts the robot into a
        // panic position

        // If we are the robot actually pressing the keypad (depth == 0)
        // Then we cannot go over position 0,0
        if (depth == 0) {
            // < Always takes priority when moving left & up
            // So, can't go 1 left from 0 or 2 left from A
            if (((prev == '0') && (cols == -1)) || ((prev == 'A') && (cols == -2))) {
                if (rows < 0) sb.append("^".repeat(abs(rows)))
                if (rows > 0) sb.append("v".repeat(abs(rows)))
                sb.append("<".repeat(abs(cols)))
            }
            // v takes priority when moving down and right
            // Nor can it go 3 down from 7 or 2 down from 4 or 1 down from 1
            else if (((prev == '7') && (rows == 3)) || ((prev == '4') && (rows == 2)) || ((prev == '1') && (rows == 1))) {
                if (cols < 0) sb.append("<".repeat(abs(cols)))
                if (cols > 0) sb.append(">".repeat(abs(cols)))
                sb.append("v".repeat(abs(rows)))
            } else {
                if (cols < 0) sb.append("<".repeat(abs(cols)))
                if (rows < 0) sb.append("^".repeat(abs(rows)))
                if (rows > 0) sb.append("v".repeat(abs(rows)))
                if (cols > 0) sb.append(">".repeat(abs(cols)))
            }
        }
        // If we are any other robot, we cannot go over position 2,0
        else {
            // That means if we are going left 2 from A or left 1 from ^
            if (((prev == 'A') && (cols == -2)) || ((prev == '^') && (cols == -1))) {
                if (rows > 0) sb.append("v".repeat(abs(rows)))
                sb.append("<".repeat(abs(cols)))
            }
            // Or up from <
            else if ((prev == '<') && (rows == -1)) {
                if (cols < 0) sb.append("<".repeat(abs(cols)))
                if (cols > 0) sb.append(">".repeat(abs(cols)))
                sb.append("^".repeat(abs(rows)))
            }
            else {
                if (cols < 0) sb.append("<".repeat(abs(cols)))
                if (rows < 0) sb.append("^".repeat(abs(rows)))
                if (rows > 0) sb.append("v".repeat(abs(rows)))
                if (cols > 0) sb.append(">".repeat(abs(cols)))
            }
        }
        sb.append("A")

        val v = calculateLowestSteps(sb.toString(), depth + 1)
        memo[Triple(prev, thisChar, depth)] = v
        return v
    }

    fun calculateLowestSteps(thisString: String, depth: Int): Long
    {
        // If we're at the bottom level it costs 1 per button press (that's me pressing it)
        if (depth == totalDepth) {
            return thisString.length.toLong()
        }

        var totalScore = 0L

        // For each character we need to press, loop through working out how to get there
        var prev = 'A'
        for (thisChar in thisString) {
            totalScore += getScoreForStep(prev, thisChar, depth)
            prev = thisChar
        }

        return totalScore
    }

}

fun main() {

    fun part1(input: List<String>): Long {

        var totalCode = 0L

        for (i in input) {
            val c = Holder(3)
            val steps = c.calculateLowestSteps(i, 0)
            println(steps)
            totalCode += i.substring(0,3).toInt() * steps
        }

        return totalCode
    }

    fun part2(input: List<String>): Long {
        var totalCode = 0L

        for (i in input) {
            val c = Holder(26)
            val steps = c.calculateLowestSteps(i, 0)
            println(steps)
            totalCode += i.substring(0,3).toInt() * steps
        }

        return totalCode
    }

    val input = readInput("Day21")
    var timeTaken = measureTime {
        part1(input).println()
    }
    println(timeTaken)

    timeTaken = measureTime {
        part2(input).println()
    }
    println(timeTaken)
}
