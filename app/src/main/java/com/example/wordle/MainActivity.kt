package com.example.wordle

import android.graphics.Color
import android.graphics.Color.parseColor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)
        val thisWord = WordChecker
//        thisWord.setWord("plop") //For testing
        val inputText = findViewById<EditText>(R.id.InputText)
        val submitRestart = findViewById<Button>(R.id.submitRestart)
        val showWord = findViewById<TextView>(R.id.SelectedWord)
        val streak = findViewById<TextView>(R.id.streak)

        //Arrays of the letter rows
        var row1 = arrayOf(
            findViewById<TextView>(R.id.Letter1_1), findViewById<TextView>(R.id.Letter1_2),
            findViewById<TextView>(R.id.Letter1_3), findViewById<TextView>(R.id.Letter1_4)
        )

        var row2 = arrayOf(
            findViewById<TextView>(R.id.Letter2_1), findViewById<TextView>(R.id.Letter2_2),
            findViewById<TextView>(R.id.Letter2_3), findViewById<TextView>(R.id.Letter2_4)
        )

        val row3 = arrayOf(
            findViewById<TextView>(R.id.Letter3_1), findViewById<TextView>(R.id.Letter3_2),
            findViewById<TextView>(R.id.Letter3_3), findViewById<TextView>(R.id.Letter3_4)
        )

        //array of all letter rows
        val rows = arrayOf(row1,  row2, row3)
        var rowCount = 0
        var currStreak = 0

        submitRestart.setOnClickListener{ //This allows the submit button to be used as a enter key
            val buttonText = submitRestart.text.toString()

            if(buttonText == "Submit"){
                showWord.visibility = View.INVISIBLE
                showWord.text = thisWord.getWord()
                val guess = inputText.text.toString().uppercase()
                var checked = checkCorrectness(thisWord, guess, rows, rowCount)

                when (checked) { //This checks for errors
                    -2 -> {
                        Toast.makeText(it.context, "Word must be 4 letters", Toast.LENGTH_SHORT)
                            .show()
                        return@setOnClickListener
                    }
                    -1 -> {
                        Toast.makeText(it.context, "Only submit letters", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                    0 -> {
                        rowCount++
                        inputText.text.clear()
                    }
                }
                if(thisWord.getWord() == guess || rowCount == 3){ //Game has completed
                    submitRestart.text = "Reset"
                    showWord.visibility = View.VISIBLE

                    if(guess == thisWord.getWord()){ //They won
                        Toast.makeText(it.context, "Congrats!", Toast.LENGTH_SHORT).show()
                        currStreak++
                    } else { //They lost
                        Toast.makeText(
                            it.context,
                            "You lost. Better luck next time!",
                            Toast.LENGTH_SHORT
                        ).show()
                        currStreak = 0
                    }
                    streak.text = currStreak.toString()
                }
            }
            else
            {
                resetGame(rows)
                rowCount = 0
                thisWord.newWord()
                submitRestart.text = "Submit"
                showWord.visibility = View.INVISIBLE
                submitRestart.text = "Submit"
            }
        }

        val themeSwitch = findViewById<Button>(R.id.modeSwitch)

        themeSwitch.setOnClickListener {
            if(themeSwitch.background.equals("drawable/simple_moon.png")){
                AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
                themeSwitch.setBackgroundResource(R.drawable.simple_sun)
            } else {
                AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
                themeSwitch.setBackgroundResource(R.drawable.simple_moon)
            }
        }
    }

    private fun checkCorrectness(word: WordChecker, guess: String, rows: Array<Array<TextView>>, rowCount: Int): Int {
        if(guess.length != 4){ //Makes sure the word is 4 letters
            return -2
        } else if (guess[0] < 'A' || guess[0] > 'Z' || //Makes sure the characters are letters
            guess[1] < 'A' || guess[1] > 'Z' ||
            guess[2] < 'A' || guess[2] > 'Z' ||
            guess[3] < 'A' || guess[3] > 'Z' ){
            return -1
        }

        val checked = word.checkWord(guess)

        val workingRow = rows[rowCount]
        for((count, item) in checked.withIndex()){
            val currCell = workingRow[count]
            when (item) { //Sets the square the appropriate color
                0 -> currCell.background = resources.getDrawable(R.drawable.green_square)
                1 -> currCell.background = resources.getDrawable(R.drawable.yellow_square)
                -1 -> currCell.background = resources.getDrawable(R.drawable.wrong_square)
            }

            currCell.text = guess[count].toString()
        }

        return 0
    }

    private fun resetGame(rows:  Array<Array<TextView>>): Unit {
        for(row in rows){ //Resets the board
            for (cell in row){
                cell.text = ""
                cell.background = resources.getDrawable(R.drawable.border)
            }
        }
    }

}