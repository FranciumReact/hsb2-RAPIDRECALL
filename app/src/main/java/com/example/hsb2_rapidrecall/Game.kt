package com.example.hsb2_rapidrecall
/* Sources:
    https://kotlinlang.org/docs/strings.html#split-strings

 */
import kotlin.random.Random

class Game {

    private var sequence = ""

    fun genSequence(length: Int): String {
        if (length < 1 || length > 10) {
            return ""
        }

        sequence = ""

        var i = 0
        while (i < length) {
            val digit = Random.nextInt(10)
            // Adds t
            sequence += digit.toString()
            i++
        }
        return sequence
    }
}