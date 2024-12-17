import kotlin.math.pow
import kotlin.time.measureTime

fun main() {

    fun getComboOperand(registers: Array<Long>, operand: Long): Long {
        if (operand < 4) return operand
        if (operand < 7) return registers[(operand - 4).toInt()]
        throw Exception()
    }

    fun adv(registers: Array<Long>, operand: Long, instruction: Int) {
        val op = 2.0.pow(getComboOperand(registers, operand).toDouble()).toLong()
        if (instruction == 0) registers[0] = registers[0] / op
        if (instruction == 6) registers[1] = registers[0] / op
        if (instruction == 7) registers[2] = registers[0] / op
    }

    fun bxl(registers: Array<Long>, operand: Long) {
        registers[1] = registers[1].xor(operand)
    }

    fun bst(registers: Array<Long>, operand: Long) {
        registers[1] = getComboOperand(registers, operand).mod(8).toLong()
    }

    fun jnz(registers: Array<Long>, index: Int, operand: Long): Int {
        var index1 = index
        if (registers[0] != 0L) {
            index1 = operand.toInt() - 2
        }
        return index1
    }

    fun bxc(registers: Array<Long>) {
        registers[1] = registers[2].xor(registers[1])
    }

    fun out(
        registers: Array<Long>,
        operand: Long
    ): Int {
        return getComboOperand(registers, operand).mod(8)
    }

    fun runProgram(
        program: List<Int>,
        registers: Array<Long>,
        programOutput: List<Int>? = null
    ): List<Int> {

        val output = mutableListOf<Int>()

        var indexInOutput = 0

        var index = 0
        while (true) {
            if (index >= program.size) {
                break
            }
            val instruction = program[index]
            val operand = program[index + 1].toLong()

            // adv (lots of types)
            if ((instruction == 0) || (instruction == 6) || (instruction == 7)) {
                adv(registers, operand, instruction)
            }
            // bxl
            else if (instruction == 1) {
                bxl(registers, operand)
            }
            // bst
            else if (instruction == 2) {
                bst(registers, operand)
            }
            // jnz
            else if (instruction == 3) {
                index = jnz(registers, index, operand)
            }
            // bxc
            else if (instruction == 4) {
                bxc(registers)
            }
            // out
            else if (instruction == 5) {
                val op = out(registers, operand)
                //if (programOutput != null) {
                //    if (op != programOutput[indexInOutput]) return "NotValid"
                //    indexInOutput++
                //}
                output.add(op)
            }
            index += 2
        }

        return output
    }

    fun part1(input: List<String>): String {
        val rx = "^Register ([ABC]): (\\d+)$".toRegex()

        val registers = arrayOf( 0L, 0L ,0L )

        val regA = rx.matchEntire(input[0])
        if (regA != null) {
            registers[0] = regA.groupValues[2].toLong()
        }
        val regB = rx.matchEntire(input[1])
        if (regB != null) {
            registers[1] = regB.groupValues[2].toLong()
        }
        val regC = rx.matchEntire(input[2])
        if (regC != null) {
            registers[2] = regC.groupValues[2].toLong()
        }

        val program = input[4].substring(9).split(",").map { it.toInt() }

        // Run program
        val output = runProgram(program, registers)

        val b = StringBuilder()
        for (i in output) {
            b.append(i.toString())
            b.append(",")
        }

        return b.substring(0, b.length - 1)
    }

    fun part2(input: List<String>): Long {
        val rx = "^Register ([ABC]): (\\d+)$".toRegex()

        val registers = arrayOf( 0L, 0L , 0L )

        val regA = rx.matchEntire(input[0])
        if (regA != null) {
            registers[0] = regA.groupValues[2].toLong()
        }
        val regB = rx.matchEntire(input[1])
        if (regB != null) {
            registers[1] = regB.groupValues[2].toLong()
        }
        val regC = rx.matchEntire(input[2])
        if (regC != null) {
            registers[2] = regC.groupValues[2].toLong()
        }

        val programString = input[4].substring(9)
        val program = programString.split(",").map { it.toInt() }

        var i = 351863867802L

        var prev = 0L
        while (i < Long.MAX_VALUE) {
            i += 16777216

            registers[0] = i
            registers[1] = 0
            registers[2] = 0
            val thisOutput = runProgram(program, registers, program)

            for (j in thisOutput.indices) {
                if (thisOutput[j] != program[j]) {
                    if (j > 6) {
                        println("Matched entries at $i - ${i - prev} : $thisOutput")
                        prev = i
                    }
                    break
                }
            }

            if (thisOutput == program) return i
        }
        return 99999
    }

    val input = readInput("Day17")
    var timeTaken = measureTime {
        part1(input).println()
    }
    println(timeTaken)

    timeTaken = measureTime {
        part2(input).println()
    }
    println(timeTaken)
}
