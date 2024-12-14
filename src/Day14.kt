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

    // For reference, all this does is output the picture, and I stopped it when I saw the Christmas tree...
    fun part2(input: List<String>): Int {
        val rx = Regex("^p=(\\d+),(\\d+) v=([\\d-]+),([\\d-]+)\$")

        val width = 101
        val height = 103

        data class Robot(val sx: Int, val sy: Int , val vx: Int, val vy: Int) {
        }

        val l = mutableListOf<Robot>()

        for (s in input) {
            val matches = rx.matchEntire(s)
            if (matches != null)
            {
                l.add(Robot(matches.groupValues[1].toInt(), matches.groupValues[2].toInt(), matches.groupValues[3].toInt(), matches.groupValues[4].toInt()))
            }
        }

        for (c in 0..10000000)
        {
            println("$c:")
            val s = mutableSetOf<Pair<Int,Int>>()
            for (r in l) {
                val fx = (r.sx + (r.vx * c)).mod(width)
                val fy = (r.sy + (r.vy * c)).mod(height)
                s.add(Pair(fx,fy))
            }

            for (row in 0..height) {
                for (col in 0..width) {
                    if (s.contains(Pair(col,row))) {
                        print("*")
                    }
                    else {
                        print(" ")
                    }
                }
                println()
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