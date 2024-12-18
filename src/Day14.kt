import kotlin.time.measureTime

fun main() {

    fun part1(input: List<String>): Int {
        val rx = Regex("^p=(\\d+),(\\d+) v=([\\d-]+),([\\d-]+)\$")

        val numberOfSteps = 100

        val width = 101
        val hWidth = width / 2
        val height = 103
        val hHeight = height / 2

        var q1 = 0
        var q2 = 0
        var q3 = 0
        var q4 = 0
        for (s in input) {
            val matches = rx.matchEntire(s)
            if (matches != null)
            {
                val sx = matches.groupValues[1].toInt()
                val sy = matches.groupValues[2].toInt()
                val vx = matches.groupValues[3].toInt()
                val vy = matches.groupValues[4].toInt()

                val fx = (sx + (vx * numberOfSteps)).mod(width)
                val fy = (sy + (vy * numberOfSteps)).mod(height)

                println("$fx,$fy")
                if ((fx < hWidth) && (fy < hHeight)) {
                    q1++
                }
                else if ((fx > hWidth) && (fy < hHeight)) {
                    q2++
                }
                else if ((fx < hWidth) && (fy > hHeight)) {
                    q3++
                }
                else if ((fx > hWidth) && (fy > hHeight)) {
                    q4++
                }
            }
        }
        println("$q1,$q2,$q3,$q4")

        return q1 * q2 * q3 * q4
    }

    // For reference, in order to get the star I just output the picture in a loop, and I stopped it when I saw the Christmas tree...
    // Since then, after looking on reddit the suggestion of using the danger level to output the picture was mentioned
    // so, I outputted the diagram every time the danger level reached a new minimum. This now shows the picture without
    // having to watch it!
    fun part2(input: List<String>): Int {
        val rx = Regex("^p=(\\d+),(\\d+) v=([\\d-]+),([\\d-]+)\$")

        val width = 101
        val hWidth = width / 2
        val height = 103
        val hHeight = height / 2


        data class Robot(val sx: Int, val sy: Int , val vx: Int, val vy: Int)

        val l = mutableListOf<Robot>()

        for (s in input) {
            val matches = rx.matchEntire(s)
            if (matches != null)
            {
                l.add(Robot(matches.groupValues[1].toInt(), matches.groupValues[2].toInt(), matches.groupValues[3].toInt(), matches.groupValues[4].toInt()))
            }
        }

        var mdl = Int.MAX_VALUE
        for (c in 0..10000000)
        {
            val s = mutableSetOf<Pair<Int,Int>>()
            var q1 = 0
            var q2 = 0
            var q3 = 0
            var q4 = 0

            for (r in l) {
                val fx = (r.sx + (r.vx * c)).mod(width)
                val fy = (r.sy + (r.vy * c)).mod(height)
                s.add(Pair(fx,fy))

                // Calculate the danger level
                if ((fx < hWidth) && (fy < hHeight)) {
                    q1++
                }
                else if ((fx > hWidth) && (fy < hHeight)) {
                    q2++
                }
                else if ((fx < hWidth) && (fy > hHeight)) {
                    q3++
                }
                else if ((fx > hWidth) && (fy > hHeight)) {
                    q4++
                }
            }

            val dl = q1 * q2 * q3 * q4

            if (dl < mdl) {
                mdl = dl

                println("$c:")
                for (row in 0..height) {
                    for (col in 0..width) {
                        if (s.contains(Pair(col, row))) {
                            print("*")
                        } else {
                            print(" ")
                        }
                    }
                    println()
                }
            }
        }

        return input.size
    }

    val input = readInput("Day14")
    var timeTaken = measureTime {
        part1(input).println()
    }
    println(timeTaken)

    timeTaken = measureTime {
        part2(input).println()
    }
    println(timeTaken)
}
