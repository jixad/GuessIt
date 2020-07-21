package com.example.guessit.ui.game

import android.os.CountDownTimer
import android.text.format.DateUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.guessit.model.Game
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue

class GameViewModel(private var isCreator: Boolean, private var isJoiner: Boolean, code: String?): ViewModel() {

    private var database: DatabaseReference = FirebaseDatabase.getInstance().reference.child("games")
    private lateinit var gameFinishListener: ValueEventListener
    private lateinit var game: Game

    private val _word = MutableLiveData<String>()
    val word: LiveData<String>
        get() = _word

    private val _code = MutableLiveData<String>()
    val code: LiveData<String>
        get() = _code

    private val _player1Score = MutableLiveData<Int>()
    val player1Score: LiveData<Int>
        get() = _player1Score

    private val _player2Score = MutableLiveData<Int>()
    val player2Score: LiveData<Int>
        get() = _player2Score

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
                    game = snapshot.getValue<Game>()!!
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
        _player1Score.value = 0
        _player2Score.value = 0
        game = Game()
        resetList()
    }

    private lateinit var wordList: MutableList<String>

    companion object {
        private const val DONE = 0L

        private const val ONE_SECOND = 1000L

        private const val COUNTDOWN_TIME = 60000L

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
                    game = snapshot.getValue<Game>()!!
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
        if (isCreator){
            _player2Score.value = (player2Score.value)?.minus(1)
            updatePlayer2Score()
        }
        if (isJoiner) {
            _player1Score.value = (player1Score.value)?.minus(1)
            updatePlayer1Score()
        }
        nextWord()
    }

    fun onCorrect() {
        if (isCreator){
            _player2Score.value = (player2Score.value)?.plus(1)
            updatePlayer2Score()
        }
        if (isJoiner) {
            _player1Score.value = (player1Score.value)?.plus(1)
            updatePlayer1Score()
        }
        nextWord()
    }

    fun onStart() {
        _disabledStartButton.value = true
        nextWord()
        timer.start()
    }

    fun endGameForCreator() {
        _eventGameFinish.value = true
    }

    fun detachListener(){
        database.child(code.value!!).removeEventListener(gameFinishListener)
    }

    private fun updatePlayer1Score(){
        database.child(code.value!!).child("player1Score").setValue(player1Score.value)
    }

    private fun updatePlayer2Score(){
        database.child(code.value!!).child("player2Score").setValue(player2Score.value)
    }

    fun setPlayer1Score(score: Int){
        _player1Score.value = score
    }

    fun setPlayer2Score(score: Int){
        _player2Score.value = score
    }

}