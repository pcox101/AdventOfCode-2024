import kotlin.time.measureTime

fun main() {

    fun parseInput(input: List<String>): Pair<List<Array<Int>>, List<List<Int>>> {
        val rules = mutableListOf<Array<Int>>()
        val updates = mutableListOf<MutableList<Int>>()

        var inRules = true
        for (s in input) {
            if (inRules) {
                if (s == "") {
                    inRules = false
                    continue
                }
                val t = s.split("|")
                rules.add(arrayOf(t[0].toInt(), t[1].toInt()))
            } else {
                val t = s.split(",")
                updates.add(t.map { it.toInt() }.toMutableList())
            }
        }
        return Pair(rules, updates)
    }

    fun part1(input: List<String>): Int {
        val (rules, updates) = parseInput(input)

        // Loop through our updates checking against the rules
        var total = 0
        for (update in updates) {
            var valid = true
            for (rule in rules) {
                val firstPos = update.indexOf(rule[0])
                val secondPos = update.indexOf(rule[1])

                if ((firstPos != -1) && (secondPos != -1)) {
                    if (firstPos > secondPos) valid = false
                }
            }
            if (valid) {
                total += update[update.size / 2]
            }
        }

        return total
    }

    fun compareTwoValues(first: Int, second: Int, rules: List<Array<Int>>): Int {
        for (rule in rules) {
            if ((first == rule[0]) && (second == rule[1]))
                return -1
            else if ((first == rule[1]) && (second == rule[0]))
                return 1
        }

        return 0
    }

    fun part2(input: List<String>): Int {
        val (rules, updates) = parseInput(input)

        val comparator = Comparator<Int> { first, second -> ( compareTwoValues(first, second, rules) ) }
        // Loop through each update, sort it then compare against the original
        var total = 0
        for (update in updates) {
            val newUpdate = mutableListOf<Int>()
            newUpdate.addAll(update)
            newUpdate.sortWith(comparator)

            if (update != newUpdate) {
                total += newUpdate[update.size / 2]
            }
        }
        return total
    }

    val input = readInput("Day05")
    var timeTaken = measureTime {
        part1(input).println()
    }
    println(timeTaken)

    timeTaken = measureTime {
        part2(input).println()
    }
    println(timeTaken)
}
