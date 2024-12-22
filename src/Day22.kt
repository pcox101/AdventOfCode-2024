import java.util.LinkedList
import kotlin.time.measureTime

fun main() {

    fun Int.mix(secretNumber: Int): Int {
        return this.xor(secretNumber)
    }

    fun Int.prune(): Int {
        return this.mod(16777216)
    }

    fun part1(input: List<String>): Long {
        var total = 0L
        for (i in input) {
            var secretNumber = i.toInt()

            for (counter in 1..2000)
            {
                secretNumber = secretNumber.mix(secretNumber * 64).prune()
                secretNumber = secretNumber.mix( secretNumber / 32).prune()
                secretNumber = secretNumber.mix(secretNumber * 2048).prune()

                //println("$counter - $secretNumber")
            }
            total += secretNumber
        }

        return total
    }

    fun LinkedList<Int>.getString(): String {
        return "${this[0]},${this[1]},${this[2]},${this[3]}"
    }

    fun part2(input: List<String>): Int {
        val totalForEachSequence = mutableMapOf<String,Int>()

        for (i in input) {
            val buyerSequenceSet = mutableSetOf<String>()

            val buyer = i.toInt()
            var secretNumber = buyer

            val last4sequence = LinkedList<Int>()
            var prev = 0

            for (counter in 1..2000)
            {
                secretNumber = secretNumber.mix(secretNumber * 64).prune()
                secretNumber = secretNumber.mix( secretNumber / 32).prune()
                secretNumber = secretNumber.mix(secretNumber * 2048).prune()

                val onesDigit = secretNumber.mod(10)
                val diff = onesDigit - prev
                prev = onesDigit
                last4sequence.add(diff)

                if (last4sequence.size > 4) {
                    last4sequence.remove()

                    // Have we seen this sequence before?
                    val hash = last4sequence.getString()
                    if (buyerSequenceSet.contains(hash))
                        continue

                    buyerSequenceSet.add(hash)
                    var k = totalForEachSequence[hash]
                    if (k == null) k = onesDigit
                    else k += onesDigit
                    totalForEachSequence[hash] = k
                }
            }
        }

        return totalForEachSequence.maxOf { it.value }

    }

    val input = readInput("Day22")
    var timeTaken = measureTime {
        part1(input).println()
    }
    println(timeTaken)

    timeTaken = measureTime {
        part2(input).println()
    }
    println(timeTaken)
}
