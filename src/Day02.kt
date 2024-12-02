import kotlin.math.abs

fun main() {
    fun isSafe(n: List<Int>): Boolean {
        val increasing = n[0] < n[1];
        var unsafe = false;
        for (i in 0..n.size - 2) {
            if ((n[i] < n[i + 1]) != increasing) {
                unsafe = true;
                break;
            }

            val diff = abs(n[i] - n[i + 1])
            if ((diff > 3) || (diff < 1)) {
                unsafe = true;
                break;
            }
        }
        return !unsafe
    }

    fun part1(input: List<String>): Int {
        var safeCounter = 0;
        for (s in input) {
            val t = s.split(" ")
            val n = t.map { it.toInt(); }

            if (isSafe(n))
            {
                safeCounter++;
            }
        }
        return safeCounter;
    }

    fun part2(input: List<String>): Int {
        var safeCounter = 0;
        for (s in input) {
            val t = s.split(" ")
            val n = t.map { it.toInt(); }

            // Safe at any level
            if (isSafe(n))
            {
                safeCounter++;
                //println("Safe at any level");
            }
            else {
                // Loop through each one, removing an element and seeing
                // If at least one is safe
                // There are much quicker ways to do this, but this is much
                // easier!
                var anySafe = false;

                for (i in n.indices) {
                    val copy = n.toMutableList();
                    copy.removeAt(i);
                    if (isSafe(copy)) {
                        anySafe = true;
                    }
                }

                if (anySafe) {
                    safeCounter++;
                }
            }
        }
        return safeCounter;
    }

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
