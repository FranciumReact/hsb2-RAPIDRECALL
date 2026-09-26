
package com.example.hsb2_rapidrecall

/*
Sources:
https://developer.android.com/develop/ui/compose/side-effects#launchedeffect
https://developer.android.com/develop/ui/compose/text/user-input
https://developer.android.com/develop/ui/compose/state

Assistance:
ChatGPT - Helped with the digit display, answer input and feedback.
*/

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.hsb2_rapidrecall.ui.theme.Hsb2RAPIDRECALLTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.LaunchedEffect

import kotlinx.coroutines.delay
import androidx.compose.ui.unit.sp

import androidx.compose.material3.OutlinedTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll

import androidx.compose.ui.modifier.modifierLocalOf
import androidx.compose.ui.text.input.KeyboardType
import java.text.SimpleDateFormat
import java.util.Locale
import androidx.compose.foundation.layout.Spacer
import androidx.compose.ui.Alignment
import java.util.Date


/**
 * Main activity for the RapidRecall app.
 * Displays the home screen, length selection and game screen.
 * Uses the Game class to generate sequences and check answers.
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            Hsb2RAPIDRECALLTheme {

                // Stores which screen the player is on
                var screen by remember { mutableStateOf("home") }

                // Stores length of sequence
                var length by remember { mutableStateOf(4) }

                // Creates Game Object, assisted by Claude
                val game = remember { Game() }

                // Stores prev attempts
                val prevAttempts = remember { PrevAttempts() }

                if (screen == "home") {
                    Start(
                        onStart = {
                            screen = "choose"
                        },
                        onPrevious = {
                            screen = "previous"
                        },
                        onSummary = {
                            screen = "summary"
                        }
                    )
                }

                if (screen == "summary"){
                    AttemptSummary(
                        prevAttempts = prevAttempts,
                        onBack = {
                            screen = "home"
                        }
                    )
                }
                if (screen == "previous"){
                    PreviousScreen(
                        prevAttempts = prevAttempts,
                        onBack = {
                            screen = "home"
                        }
                    )
                }
                if (screen == "choose") {
                    ChooseLength(
                        onBack = {
                            screen = "home"
                        },
                        onPlay = { selectedLength ->
                            length = selectedLength
                            game.genSequence(length)
                            screen = "game"
                        }
                    )
                }

                if (screen == "game") {
                    PlayGame(
                        game = game,
                        prevAttempts = prevAttempts,
                        onBack = {
                            screen = "home"
                        }
                    )
                }
            }
        }
    }
}


@Composable
fun Start(
    onStart: () -> Unit,
    onPrevious: () -> Unit,
    onSummary: () -> Unit
    ) {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "RapidRecall"
        )

        Button(
            onClick = {
                onStart()
            }
        ) {
            Text(
                text = "Start Game"
            )
        }

        Button(
            onClick = {
                onPrevious()
            }
        ) {
            Text(
                text = "Previous Attempts"
            )
        }

        Button(
            onClick = {
                onSummary()
            }
        ) {
            Text(
                text = "Attempt Summary"
            )
        }
    }
}


@Composable
fun ChooseLength(
    onBack: () -> Unit,

    // Allows main activity to see selected length
    onPlay: (Int) -> Unit
) {

    // Stores sequence length starting at 4
    var length by remember { mutableStateOf(4) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),

        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Choose Sequence Length"
        )

        Text(
            text = "Length can be between 1 and 10 digits"
        )

        Text(
            text = length.toString()
        )

        Row {

            // Button to decrease length
            Button(
                onClick = {
                    if (length > 1) {
                        length--
                    }
                }
            ) {
                Text(
                    text = "-"
                )
            }

            // Button to increase length
            Button(
                onClick = {
                    if (length < 10) {
                        length++
                    }
                }
            ) {
                Text(
                    text = "+"
                )
            }
        }

        Button(
            onClick = {
                onPlay(length)
            }
        ) {
            Text(
                text = "Play"
            )
        }

        Button(
            onClick = {
                onBack()
            }
        ) {
            Text(
                text = "Back"
            )
        }
    }
}


/**
 * Displays the generated sequence one digit at a time.
 * After the sequence is shown, the player enters an answer.
 * Uses Game.kt to check if the answer is correct.
 * The sequence display uses LaunchedEffect and delay.
 * Previous attempts are stores
 */
@Composable
fun PlayGame(
    game: Game,
    onBack: () -> Unit,
    prevAttempts: PrevAttempts
    ) {

    // Gets the sequence generated
    val sequence = game.getSequence()

    // Tracks digit displayed
    var digit by remember { mutableStateOf(0) }

    // Checks if all digits are displayed
    var done by remember { mutableStateOf(false) }

    // Stores what the player types
    var answer by remember { mutableStateOf("") }

    // Checks if the player has submitted
    var submitted by remember { mutableStateOf(false) }

    // Stores if the player got the answer right
    var correct by remember { mutableStateOf(false) }

    // Assisted by ChatGPT
    LaunchedEffect(sequence) {

        digit = 0
        done = false

        while (digit < sequence.length) {

            // Adds delay for each digit shown
            delay(1000)

            digit++
        }

        done = true
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Can you remember this sequence?"
        )

        if (!done) {

            Text(
                text = sequence[digit].toString(),
                fontSize = 60.sp
            )

        } else {

            // Checks if the player has submitted an answer
            if (!submitted) {

                Text(
                    text = "Enter your answer"
                )

                // Input box for the player's answer
                // Used ChatGPT for assistance with the text field
                OutlinedTextField(
                    value = answer,

                    onValueChange = { input ->

                        // Checks if the input only has numbers
                        if (input.all { it.isDigit() }) {

                            // Stores what the player typed
                            answer = input
                        }
                    },

                    label = {
                        Text(
                            text = "Your Answer"
                        )
                    },

                    // Opens the number keyboard
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),

                    // Keeps the input on one line
                    singleLine = true
                )

                // Button to submit the player's answer
                Button(
                    onClick = {

                        // Checks the answer using Game.kt
                        correct = game.checkAnswer(answer)

                        val attempt = RecordAttempt(
                            sequence.length,
                            sequence,
                            answer,
                            correct,
                            System.currentTimeMillis()
                        )

                        prevAttempts.addAttempt(attempt)
                        // Marks the answer as submitted
                        submitted = true
                    },

                    // Only allows submitting if something was entered
                    enabled = answer.isNotEmpty()
                ) {
                    Text(
                        text = "Submit"
                    )
                }

            } else {

                // Shows if the player got the answer right
                if (correct) {
                    Text(
                        text = "Correct"
                    )
                } else {
                    Text(
                        text = "Incorrect"
                    )
                }

                // Shows the sequence generated by the game
                Text(
                    text = "Correct answer: $sequence"
                )

                // Shows what the player entered
                Text(
                    text = "Your answer: $answer"
                )
            }
        }

        Button(
            onClick = {
                onBack()
            }
        ) {
            Text(
                text = "Back"
            )
        }
    }
}

@Composable
fun PreviousScreen(
    prevAttempts: PrevAttempts,
    onBack: () -> Unit
    ){
    val attempts = prevAttempts.getAttempts()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = "Previous Attempts",
            fontSize = 24.sp
        )

        // Checks if there is a record of previous attempts
        if (attempts.isEmpty()){
            Text(
                text = "No previous attempts"
            )
        } else {
            // Gets each recorded game
            for (attempt in attempts) {
                Text(
                    text = "Length: ${attempt.length}"
                )

                Text(
                    text = "Correct Sequence: ${attempt.sequence}"
                )

                Text(
                    text = "Your Answer: ${attempt.answer}"
                )

                if (attempt.correctSeq) {
                    Text(
                        text = "Result: Correct"
                    )
                } else {
                    Text(
                        text = "Result: Incorrect"
                    )
                }

                // Gets date
                val date = SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss",
                    Locale.getDefault()
                )

                Text(
                    text = "Time: ${date.format(Date(attempt.time))}"
                )

                Spacer(
                    modifier = Modifier.height(20.dp)
                )
            }
        }

        Button(
            onClick = {
                onBack()
            }
        ){
            Text(
                text = "Back"
            )
        }
    }
}

@Composable
fun AttemptSummary(
    prevAttempts: PrevAttempts,
    onBack: () -> Unit
    ) {
    // total games
    val total = prevAttempts.totalAttempts()

    // Number of correct answers
    val correct = prevAttempts.totalCorrectAttempts()

    // Accuracy
    val accuracy = prevAttempts.attemptAccuracy()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),

        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = "Attempt Summary",
            fontSize = 24.sp
        )

        // Displays total attempts
        Text(
            text = "Total Attempts: $total"
        )

        // Displays # of correct attempts
        Text(
            text = "Correct Attempts: $correct"
        )

        // Displays accuracy percentage
        Text(
            text = "Accuracy: ${"%.1f".format(accuracy)}%"
        )

        Button(
            onClick = {
                onBack()
            }
        ) {
            Text(
                text = "Back"
            )
        }
    }
}