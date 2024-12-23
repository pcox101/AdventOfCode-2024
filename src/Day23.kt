import java.util.*
import kotlin.time.measureTime

fun List<String>.getUniqueKey(): String {
    val sorted = this.sorted()
    val sb = StringBuilder()
    for (i in sorted) {
        sb.append("$i,")
    }
    return sb.toString()
}

fun Set<String>.getUniqueKey(): String {
    val sorted = this.sorted()
    val sb = StringBuilder()
    for (i in sorted) {
        sb.append("$i,")
    }
    return sb.toString()
}

fun List<String>.containsStartingWithT(): Boolean {
    for (t in this) {
        if (t.startsWith("t")) return true
    }
    return false
}

fun main() {

    val mapOfComputers = mutableMapOf<String, MutableSet<String>>()

    fun populateMapOfComputers(
        input: List<String>
    ) {
        for (i in input) {
            val s = i.split("-")
            var firstComputer = mapOfComputers[s[0]]
            if (firstComputer == null)
                firstComputer = mutableSetOf(s[1])
            else
                firstComputer.add(s[1])
            mapOfComputers[s[0]] = firstComputer

            var secondComputer = mapOfComputers[s[1]]
            if (secondComputer == null)
                secondComputer = mutableSetOf(s[0])
            else
                secondComputer.add(s[0])
            mapOfComputers[s[1]] = secondComputer
        }
    }

    fun part1(): Int {

        val included = mutableSetOf<String>()
        for (c in mapOfComputers) {
            // I have a set of linked computers, if there is a pair that are themselves linked, then I have a triple
            for (lc in c.value) {
                val compareSet = c.value.toMutableSet()
                compareSet.remove(lc)

                // Now see if lc is in any of the linked computers from compareSet
                for (llc in compareSet) {
                    if (mapOfComputers[llc]!!.contains(lc)) {
                        val v = listOf(c.key,lc,llc)
                        if (v.containsStartingWithT())
                            included.add(v.getUniqueKey())
                    }
                }
            }
        }

        return included.size
    }

    fun Set<String>.isFullyConnected(): Boolean {
        // Is everything in this set linked to everything else in the set?
        var allLinked = true
        for (i in this) {
            val compareSet = mapOfComputers[i]!!.toMutableSet()
            compareSet.add(i)
            if (compareSet.intersect(this).size != this.size) {
                allLinked = false
                break
            }
        }
        return allLinked
    }

    fun getMaxConnections(): String {

        // What we are going to do is build up connected networks from each node
        // until we have found one that is fully connected.

        var maxConnections = 0
        var longestString = ""

        val q = LinkedList<Set<String>>()
        val visited = mutableSetOf<String>()

        for (c in mapOfComputers) {
            val s = setOf(c.key)
            q.add(s)
            visited.add(s.getUniqueKey())
        }

        while (q.isNotEmpty()) {
            val entry = q.remove()

            // Is this set of nodes fully connected?
            if (entry.isFullyConnected()) {
                if (entry.size > maxConnections) {
                    maxConnections = entry.size
                    longestString = entry.getUniqueKey()
                }

                // Still connected, now add an entry for each node connected to all the current nodes (this is the
                // intersection of all the sets of connected nodes
                var connectedNodes = mapOfComputers[entry.first()]!!.toMutableSet()
                for (c in entry) {
                    connectedNodes = connectedNodes.intersect(mapOfComputers[c]!!).toMutableSet()
                }

                for (c in connectedNodes) {
                    val newSet = entry.toMutableSet()
                    newSet.add(c)
                    val k = newSet.getUniqueKey()
                    if (!visited.contains(k)) {
                        visited.add(k)
                        q.add(newSet)
                    }
                }
            }
        }

        return longestString
    }

    fun part2(): String {

        // Need to find the largest set of fully connected computers
        val total = getMaxConnections()
        return total
    }

    val input = readInput("Day23")

    populateMapOfComputers(input)
    var timeTaken = measureTime {
        part1().println()
    }
    println(timeTaken)

    timeTaken = measureTime {
        part2().println()
    }
    println(timeTaken)
}
