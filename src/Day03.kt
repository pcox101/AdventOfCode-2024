import kotlin.math.abs

fun main() {
    fun getTotal(rx: Regex, line: String): Int {
        val matches = rx.findAll(line);
        var total = 0;
        for (match in matches) {
            total += match.groupValues[1].toInt() * match.groupValues[2].toInt()
        }
        return total
    }

    fun part1(input: List<String>): Int {
        val rx = Regex("mul\\((\\d{1,3}),(\\d{1,3})\\)")
        var total = 0;
        for (line in input) {
            total += getTotal(rx, line)
        }

        return total
    }

    fun part2(input: List<String>): Int {
        val rx = Regex("mul\\((\\d{1,3}),(\\d{1,3})\\)")
        val doordont = Regex("do\\(\\)|don't\\(\\)")

        var inMult = true;
        var total = 0

        for (line in input) {
            val matches = doordont.findAll(line).toList();
            var split = doordont.split(line);

            for (i in 0..<split.size) {
                // Get the match that starts this part
                if (i != 0)
                {
                    inMult = matches[i - 1].value == "do()"
                }
                if (inMult) {
                    total += getTotal(rx, split[i])
                }
            }
        }

        return total
    }

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}
