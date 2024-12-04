import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/$name.txt").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

fun buildGameBoardFromInput(input: List<String>): Array<CharArray> {
    val height = input.size
    val width = input[0].length

    var gameboard = Array(height) { CharArray(width) }

    for (x in 0..<width) {
        for (y in 0..<height) {
            gameboard[x][y] = input[y][x];
        }
    }
    return gameboard;
}

