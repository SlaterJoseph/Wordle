package com.example.wordle

object WordChecker {
    private var word = "";

    init {
        word = FourLetterWordList.getRandomFourLetterWord()
    }

    //reset the word
    fun newWord(): Unit { word = FourLetterWordList.getRandomFourLetterWord() }

    //getter and setter for testing
    public fun getWord(): String { return word; }
    public fun setWord(choice: String): Unit { word = choice; }

    public fun checkWord(guess: String): Array<Int> {
        val chars = Array(26){0}; //For base word
        val results = Array(4){-1}; //For results
        val test = arrayOf(guess[0], guess[1], guess[2], guess[3]);

        for(c in word.toCharArray()) chars[c - 'A']++ //Adds the letters from out base word to our array

        //bass
        //barn

        for((i, c) in test.withIndex()) if(c == word[i]){ //Correct Letter and location
            results[i] = 0
            chars[c - 'A']--
        }

        //Two loops prevent errors with words like clop and entering guesses like crop where the
        //previous method would get a return array of [1,0,0,0] and not [2,0,0,0] like it should

        for((i, c) in test.withIndex()){
            if(results[i] == 0) continue;
            else if (chars[c - 'A'] > 0){ //Correct letter, incorrect location
                results[i] = 1;
                chars[c - 'A']--; //decrement the letter in case it appears twice
            } else {
                results[i] == 2 //Incorrect letter
            }
        }
        return results;
    }
}