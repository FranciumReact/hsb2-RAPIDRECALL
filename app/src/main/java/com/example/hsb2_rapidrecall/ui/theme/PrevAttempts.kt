package com.example.hsb2_rapidrecall.ui.theme
/*
Source:
    https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/mutable-list-of.html
    https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/to-list.html
 */

/**
 * Stores all game attempts
 * Private list prevents other classes from changing it
 * Functions to view previous attempts and attempt stats
 * No issues
 */
import com.example.hsb2_rapidrecall.RecordAttempt

class PrevAttempts {

    private val attempts = mutableListOf<RecordAttempt>()

    fun addAttempt(attempt: RecordAttempt){
        attempts.add(attempt)
    }

    // Returns a list of all the previous attempts
    fun getAttempts(): List<RecordAttempt>{
        return attempts.toList()
    }

    fun totalAttempts(): Int{
        return attempts.size
    }

    fun totalCorrectAttempts(): Int{
        var total = 0
        // Gets each attempt and checks if it was the correct sequence
        for (attempt in attempts){
            if (attempt.correctSeq){
                total++
            }
        }
        return total
    }

    // Gets the percent accuracy
    fun attemptAccuracy(): Double{
        if (attempts.isEmpty()) return 0.0

        val correct = totalCorrectAttempts()
        val total = totalAttempts()

        return correct.toDouble()/ total * 100
    }
}