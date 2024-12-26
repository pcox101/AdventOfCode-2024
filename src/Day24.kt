import java.util.*
import kotlin.time.measureTime

class Gate(val input1: String, val input2: String, val output: String, val type: String) {
    override fun toString(): String {
        return "$input1 $type $input2 -> $output"
    }
}

fun main() {

    val memo = mutableMapOf<String,Boolean>()
    var gateOutputMap = mutableMapOf<String,Gate>()

    fun getGateOutputValue(gateName: String): Boolean {
        val c = memo[gateName]
        if (c != null)
            return c

        if ((gateName.startsWith("y")) || gateName.startsWith("x"))
            return false

        val gate = gateOutputMap[gateName]!!

        val i1 = getGateOutputValue(gate.input1)
        val i2 = getGateOutputValue(gate.input2)

        var output = false
        if (gate.type == "AND") output = i1 and i2
        if (gate.type == "XOR") output = i1 xor i2
        if (gate.type == "OR") output = i1 or i2

        memo[gateName] = output
        return output
    }

    fun part1(input: List<String>): Long {
        val inputRx = Regex("^([\\d|\\w]*): ([0|1])\$")
        val gateRx = Regex("^([\\d|\\w]*) (AND|XOR|OR) ([\\d|\\w]*) -> ([\\d|\\w]*)\$")

        val inputMap = mutableMapOf<String,Boolean>()

        for (i in input) {
            val inputMatches = inputRx.matchEntire(i)
            val gateMatches = gateRx.matchEntire(i)

            if (inputMatches != null) {
                inputMap[inputMatches.groupValues[1]] = (inputMatches.groupValues[2] == "1")
            }
            if (gateMatches != null) {
                val g = Gate(gateMatches.groupValues[1],gateMatches.groupValues[3], gateMatches.groupValues[4], gateMatches.groupValues[2])
                gateOutputMap[gateMatches.groupValues[4]] = g
            }
        }

        memo.putAll(inputMap)

        // Loop through all the z in order
        val sb = StringBuilder()

        for (gate in gateOutputMap.keys.filter { it.startsWith("z") }.sortedDescending()) {
            val value = getGateOutputValue(gate)
            sb.append(if (value) "1" else "0")
        }

        // Convert sb from binary
        return sb.toString().toLong(2)
    }

    fun getChildren(parent: String, depth: Int): Set<String> {
        val q = LinkedList<Pair<String,Int>>()
        val o = mutableSetOf<String>()

        q.add(Pair(parent,depth))

        while (q.isNotEmpty()) {
            val entry = q.remove()

            if (entry.second == 0) continue
            //if (entry.first.startsWith("x") || entry.first.startsWith("y")) continue
            if (!entry.first.startsWith("z")) {
                o.add(entry.first)
            }

            val g = gateOutputMap[entry.first]
            if (g != null) {
                q.add(Pair(g.input1, entry.second - 1))
                q.add(Pair(g.input2, entry.second - 1))
            }
        }

        return o
    }

    fun part2(input: List<String>): String {
        val inputRx = Regex("^([\\d|\\w]*): ([0|1])\$")
        val gateRx = Regex("^([\\d|\\w]*) (AND|XOR|OR) ([\\d|\\w]*) -> ([\\d|\\w]*)\$")

        // I created these by hand by analysing the maps on a piece of paper...
        val swaps = mapOf(Pair("gdd","z05"),Pair("z05","gdd"),Pair("cwt","z09"),Pair("z09","cwt"),Pair("jmv","css"),Pair("css","jmv"),Pair("pqt","z37"),Pair("z37","pqt"))
        //val swaps = mapOf<String,String>()

        val inputMap = mutableMapOf<String, Boolean>()

        for (i in input) {
            val inputMatches = inputRx.matchEntire(i)
            val gateMatches = gateRx.matchEntire(i)

            if (inputMatches != null) {
                inputMap[inputMatches.groupValues[1]] = false // (inputMatches.groupValues[2] == "1")
            }
            if (gateMatches != null) {

                var output = gateMatches.groupValues[4]
                if(swaps.containsKey(output))
                {
                    output = swaps[output]!!
                }

                val g = Gate(gateMatches.groupValues[1], gateMatches.groupValues[3], output, gateMatches.groupValues[2])
                gateOutputMap[output] = g
            }
        }

        val setOfError = mutableSetOf<String>()

        for (i in 0..44) {
            val n = "%02d".format(i)
            val nPlus1 = "%02d".format(i + 1)

            // Check the straight output and the carry
            val testArray = arrayOf(
                Pair(Pair(false, false), Pair(false, false)),
                Pair(Pair(false, true), Pair(true, false)),
                Pair(Pair(true, false), Pair(true, false)),
                Pair(Pair(true, true), Pair(false, true))
            )

            var hasError = false
            var hasCarryError = false
            for (entry in testArray) {

                memo.clear()
                memo["x$n"] = entry.first.first
                memo["y$n"] = entry.first.second

                val output = getGateOutputValue("z$n")
                val carry = getGateOutputValue("z$nPlus1")

                if (output != entry.second.first)  {
                    hasError = true
                }
                if  (carry != entry.second.second) {
                    hasCarryError = true
                }
            }

            if ((hasError) || (hasCarryError)) {
                if (hasError) println("Error at z$n.")
                else println("Carry error at z$n.")
                // Print out the values of the gates leading to this one
                val v = getChildren("z$n", 3)

                for (g in v) {
                    val v = gateOutputMap[g]
                    println(v)
                }
            }
        }

        // Get our swaps and sort them alphabetically
        val o = swaps.keys.sorted()
        val sb = StringBuilder()
        for (k in o) sb.append("$k,")

        return sb.toString()
    }

    val input = readInput("Day24")
    var timeTaken = measureTime {
        part1(input).println()
    }
    println(timeTaken)

    timeTaken = measureTime {
        part2(input).println()
    }
    println(timeTaken)
}
