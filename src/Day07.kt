import java.math.BigInteger
import kotlin.time.measureTime

fun main() {

    fun canParsePart1(result: BigInteger, list: List<BigInteger>) : Boolean
    {
        if (list.size == 1)
        {
            return list[0] == result
        }
        if (list[0] > result) return false

        val newList = list.subList(2, list.size)
        val newTimesList = mutableListOf(list[0] * list[1])
        newTimesList.addAll(newList)
        val newAddList = mutableListOf(list[0] + list[1])
        newAddList.addAll(newList)

        return canParsePart1(result, newTimesList) || canParsePart1(result, newAddList)
    }

    fun part1(input: List<String>): BigInteger {
        val rx = Regex("^(\\d*): ((\\d* ?)*)\$")
        var total = 0.toBigInteger()
        for (s in input) {
            val matches = rx.matchEntire(s)
            if (matches != null) {
                val result = matches.groupValues[1].toBigInteger()
                val list = matches.groupValues[2].split(" ").map { it.toBigInteger() }

                if (canParsePart1(result, list)) {
                    total += result
                }
            } else {
                println("Invalid String")
            }
        }
        return total
    }

    fun canParsePart2(result: BigInteger, list: List<BigInteger>) : Boolean
    {
        if (list.size == 1)
        {
            return list[0] == result
        }
        if (list[0] > result) return false

        val newList = list.subList(2, list.size)
        val newTimesList = mutableListOf(list[0] * list[1])
        newTimesList.addAll(newList)
        val newAddList = mutableListOf(list[0] + list[1])
        newAddList.addAll(newList)
        val newConcatenateList = mutableListOf((list[0].toString() + list[1].toString()).toBigInteger())
        newConcatenateList.addAll(newList)

        return canParsePart2(result, newTimesList)
                    || canParsePart2(result, newAddList)
                    || canParsePart2(result, newConcatenateList)
    }

    fun part2(input: List<String>): BigInteger {
        val rx = Regex("^(\\d*): ((\\d* ?)*)\$")
        var total = 0.toBigInteger()
        for (s in input) {
            val matches = rx.matchEntire(s)
            if (matches != null) {
                val result = matches.groupValues[1].toBigInteger()
                val list = matches.groupValues[2].split(" ").map { it.toBigInteger() }

                if (canParsePart2(result, list)) {
                    total += result
                }
            } else {
                println("Invalid String")
            }
        }
        return total
    }

    val input = readInput("Day07")
    var timeTaken = measureTime {
        part1(input).println()
    }
    println(timeTaken)

    timeTaken = measureTime {
        part2(input).println()
    }
    println(timeTaken)
}
