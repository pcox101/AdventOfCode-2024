fun main() {

    fun parseInput(input: List<String>): Pair<MutableList<Array<Int>>, MutableList<MutableList<Int>>> {
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
                updates.add(t.map({ it.toInt() }).toMutableList())
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

    fun part2(input: List<String>): Int {
        val (rules, updates) = parseInput(input)

        // Loop through, and if it doesn't match then sort it until it does
        var total = 0
        for (update in updates) {
            var anyInvalid = false
            var index = 0
            while (index < rules.size) {
                val rule = rules[index]
                val firstPos = update.indexOf(rule[0])
                val secondPos = update.indexOf(rule[1])

                if ((firstPos != -1) && (secondPos != -1)) {
                    if (firstPos > secondPos) {
                        anyInvalid = true
                        val stack = update[secondPos]
                        update[secondPos] = update[firstPos]
                        update[firstPos] = stack
                        index = 0
                    }
                    else
                    {
                        index++
                    }
                }
                else {
                    index++
                }
            }
            if (anyInvalid) {
                total += update[update.size / 2]
            }
        }

        return total
    }

    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}
