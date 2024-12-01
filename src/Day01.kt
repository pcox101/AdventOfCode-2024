import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Int {
        val rx = Regex("^(\\d*)\\s*(\\d*)\$");
        var diff  = 0;

        val l1 = mutableListOf<Int>()
        val l2 = mutableListOf<Int>();

        for (s in input) {
            val matches = rx.matchEntire(s);

            if (matches != null) {
                l1.add(matches.groupValues[1].toInt());
                l2.add(matches.groupValues[2].toInt());
            }
            else
            {
                println("Invalid string");
            }
        }

        l1.sort();
        l2.sort();

        for (i in 0..l1.size - 1)
        {
            diff += abs(l1[i] - l2[i]);
        }

        return diff;
    }

    fun part2(input: List<String>): Int {
        val rx = Regex("^(\\d*)\\s*(\\d*)\$");

        val l1 = mutableListOf<Int>()
        val l2 = mutableListOf<Int>();

        for (s in input) {
            val matches = rx.matchEntire(s);

            if (matches != null) {
                l1.add(matches.groupValues[1].toInt());
                l2.add(matches.groupValues[2].toInt());
            } else {
                println("Invalid string");
            }
        }

        var similarity = 0;
        for (i in 0..l1.size - 1) {
            var thisScore = l2.count{it == l1[i]};
            similarity += l1[i] * thisScore;
        }

        return similarity;
    }

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
