import kotlin.time.measureTime

fun main() {

    fun getNumberOfSteps(buttonAOffset: Pair<Int, Int>, buttonBOffset: Pair<Int, Int>, target: Pair<Long, Long>): Long {

        // This is two simultaneous equations
        // targetX = (buttonAXOffset * buttonACounter) + (buttonBXOffset * buttonBCounter)
        // targetY = (buttonAYOffset * buttonACounter) + (buttonBYOffset * buttonBCounter)

        // Solved on paper...
        // a = (buttonBXOffset * targetY - buttonBY * targetX) / (buttonBXOffset * buttonAYOffset - buttonBYoOffset * buttonAXOffset)

        val numerator = (buttonBOffset.first * target.second) - (buttonBOffset.second * target.first)
        val denominator =  (buttonBOffset.first * buttonAOffset.second) - (buttonBOffset.second * buttonAOffset.first)

        val aPresses = numerator / denominator
        val bPresses = (target.first - (buttonAOffset.first * aPresses)) / buttonBOffset.first

        val xCheck = (aPresses * buttonAOffset.first) + (bPresses * buttonBOffset.first)
        val yCheck = (aPresses * buttonAOffset.second) + (bPresses * buttonBOffset.second)

        if ((xCheck == target.first) && (yCheck == target.second)) {
            val t = (aPresses * 3) + bPresses
            //println("Solvable - $t")
            return t
        }

        //println("Not solvable")
        return 0
    }

    fun part1(input: List<String>): Long {
        val button = Regex("^Button ([AB]): X\\+(\\d+), Y\\+(\\d+)$")
        val prize = Regex("^Prize: X=(\\d+), Y=(\\d+)\$")

        var buttonAOffset = Pair(0,0)
        var buttonBOffset = Pair(0,0)
        var target = Pair(0L,0L)
        var numberOfSteps = 0L
        for (s in input)
        {
            val buttonMatches = button.matchEntire(s)
            val prizeMatches = prize.matchEntire(s)

            if (s == "")
            {
                numberOfSteps += getNumberOfSteps(buttonAOffset, buttonBOffset, target)
            }

            if (buttonMatches != null)
            {
                val thisPair = Pair(buttonMatches.groupValues[2].toInt(), buttonMatches.groupValues[3].toInt())
                if (buttonMatches.groupValues[1] == "B")
                {
                    buttonBOffset = thisPair
                }
                else
                {
                    buttonAOffset = thisPair
                }
            }
            if (prizeMatches != null)
            {
                target = Pair(prizeMatches.groupValues[1].toLong(), prizeMatches.groupValues[2].toLong())
            }
        }
        numberOfSteps += getNumberOfSteps(buttonAOffset, buttonBOffset, target)
        return numberOfSteps
    }

    fun part2(input: List<String>): Long {
        val button = Regex("^Button ([AB]): X\\+(\\d+), Y\\+(\\d+)$")
        val prize = Regex("^Prize: X=(\\d+), Y=(\\d+)\$")

        var buttonAOffset = Pair(0,0)
        var buttonBOffset = Pair(0,0)
        var target = Pair(0L,0L)
        var numberOfSteps = 0L
        for (s in input)
        {
            val buttonMatches = button.matchEntire(s)
            val prizeMatches = prize.matchEntire(s)

            if (s == "")
            {
                numberOfSteps += getNumberOfSteps(buttonAOffset, buttonBOffset, target)
            }

            if (buttonMatches != null)
            {
                val thisPair = Pair(buttonMatches.groupValues[2].toInt(), buttonMatches.groupValues[3].toInt())
                if (buttonMatches.groupValues[1] == "B")
                {
                    buttonBOffset = thisPair
                }
                else
                {
                    buttonAOffset = thisPair
                }
            }
            if (prizeMatches != null)
            {
                target = Pair(prizeMatches.groupValues[1].toLong(), prizeMatches.groupValues[2].toLong())
                target = Pair(target.first + 10000000000000, target.second + 10000000000000)
            }
        }
        numberOfSteps += getNumberOfSteps(buttonAOffset, buttonBOffset, target)

        return numberOfSteps
    }

    val input = readInput("Day13")
    var timeTaken = measureTime {
        part1(input).println()
    }
    println(timeTaken)

    timeTaken = measureTime {
        part2(input).println()
    }
    println(timeTaken)
}
