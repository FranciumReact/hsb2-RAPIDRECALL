package com.example.hsb2_rapidrecall

/**
 * Stores the information from one attempt
 * class helps keep the whole code organized
 * No issues
 */
class RecordAttempt (
    val length: Int,
    val sequence: String,
    val answer: String,
    val correctSeq: Boolean,
    val time: Long
)