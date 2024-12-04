fun main() {
    fun getTotal(rx: Regex, line: String): Int {
        val matches = rx.findAll(line)
        var total = 0
        for (match in matches) {
            total += match.groupValues[1].toInt() * match.groupValues[2].toInt()
        }
        return total
    }

    fun part1(input: List<String>): Int {
        val rx = Regex("mul\\((\\d{1,3}),(\\d{1,3})\\)")
        var total = 0
        for (line in input) {
            total += getTotal(rx, line)
        }

        return total
    }

    fun part2(input: List<String>): Int {
        val rx = Regex("mul\\((\\d{1,3}),(\\d{1,3})\\)|do\\(\\)|don't\\(\\)")

        var doMultiply = true
        var total = 0

        for (line in input) {
            var match = rx.find(line)
            while (match != null)
            {
                if (match.value == "do()")
                    doMultiply = true
                else if (match.value == "don't()")
                    doMultiply = false
                else
                {
                    if (doMultiply)
                        total += match.groupValues[1].toInt() * match.groupValues[2].toInt()
                }
                match = match.next()
            }
        }

        return total
    }

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}
