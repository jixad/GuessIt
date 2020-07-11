package com.example.guessit.ui.game

import android.content.ContentValues.TAG
import android.os.CountDownTimer
import android.text.format.DateUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.guessit.model.Game
import com.google.firebase.database.*

class GameViewModel(isCreator: Boolean, isJoiner: Boolean, code: String?): ViewModel() {

    private var database: DatabaseReference = FirebaseDatabase.getInstance().reference.child("games")
    private lateinit var gameFinishListener: ValueEventListener

    private val _word = MutableLiveData<String>()
    val word: LiveData<String>
        get() = _word

    private val _code = MutableLiveData<String>()
    val code: LiveData<String>
        get() = _code

    private val _score = MutableLiveData<Int>()
    val score: LiveData<Int>
        get() = _score

    private val _currentTime = MutableLiveData<Long>()
    private val currentTime: LiveData<Long>
        get() = _currentTime

    private val _eventGameFinish = MutableLiveData<Boolean>()
    val eventGameFinish: LiveData<Boolean>
        get() = _eventGameFinish

    private val _player2Turn = MutableLiveData<Boolean>()
    val player2Turn: LiveData<Boolean>
        get() = _player2Turn

    private val _disabledStartButton = MutableLiveData<Boolean>()
    val disabledStartButton: LiveData<Boolean>
        get() = _disabledStartButton

    val currentTimeString = Transformations.map(currentTime) { time ->
        DateUtils.formatElapsedTime(time)
    }

    private val timer: CountDownTimer =  object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND) {

        override fun onTick(millisUntilFinished: Long) {
            _currentTime.value = millisUntilFinished / ONE_SECOND
        }

        override fun onFinish() {
            if (isCreator){
                _player2Turn.value = true
                updateGameTurn()
            }
            if(isJoiner){
                finishGame()
            }
        }
    }

    fun updateGameTurn(){
        database.child(code.value!!).addValueEventListener(object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val game = Game()
                    game.player1 = snapshot.child("player1").value.toString()
                    game.player2 = snapshot.child("player2").value.toString()
                    game.code = snapshot.child("code").value.toString()
                    game.isPlayer2Turn = true
                    database.child(code.value!!).setValue(game)
                }
            }
        })
    }

    init {
        if (isCreator){
            _player2Turn.value = false
        }
        _code.value = code
        _eventGameFinish.value = false
    }

    private lateinit var wordList: MutableList<String>

    companion object {

        // Time when the game is over
        private const val DONE = 0L

        // Countdown time interval
        private const val ONE_SECOND = 1000L

        // Total time for the game
        private const val COUNTDOWN_TIME = 6000L

    }

    override fun onCleared() {
        super.onCleared()
        timer.cancel()
    }

    private fun finishGame(){
        gameFinishListener = object: ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val game = Game()
                    game.player1 = snapshot.child("player1").value.toString()
                    game.player2 = snapshot.child("player2").value.toString()
                    game.code = snapshot.child("code").value.toString()
                    game.isPlayer2Turn = true
                    game.isGameFinished = true
                    database.child(code.value!!).setValue(game)
                    _eventGameFinish.value = true
                }
            }
        }
        database.child(code.value!!).addValueEventListener(gameFinishListener)
    }

    private fun resetList() {
        wordList = mutableListOf(
            "queen",
            "hospital",
            "basketball",
            "cat",
            "change",
            "snail",
            "soup",
            "calendar",
            "sad",
            "desk",
            "guitar",
            "home",
            "railway",
            "zebra",
            "jelly",
            "car",
            "crow",
            "trade",
            "bag",
            "roll",
            "bubble"
        )
        wordList.shuffle()
    }

    private fun nextWord() {
        if (wordList.isNotEmpty()) {
            _word.value = wordList.removeAt(0)
        } else {
            resetList()
        }
    }

    fun onSkip() {
        _score.value = (score.value)?.minus(1)
        nextWord()
    }

    fun onCorrect() {
        _score.value = (score.value)?.plus(1)
        nextWord()
    }

    fun onStart() {
        _disabledStartButton.value = true
        timer.start()
    }

    fun endGameForCreator() {
        _eventGameFinish.value = true
    }

    fun detachListener(){
        Log.i("MAL", "DETACHED")
        database.child(code.value!!).removeEventListener(gameFinishListener)
    }


}