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
            // Converts the number to a string
            sequence += digit.toString()
            i++
        }
        return sequence
    }

    // Function that checks if the sequence is equal to the answer or not
    fun checkAnswer(answer:String): Boolean {
        // Checks if there is a sequence
        if (sequence == "") return false

        if (answer == sequence) return true

        return false
    }

    // Helper function to get the sequence
    fun getSequence(): String {
        return sequence
    }
}