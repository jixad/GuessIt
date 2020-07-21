package com.example.guessit.ui.join

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.guessit.model.Game
import com.google.firebase.database.*


class JoinViewModel: ViewModel() {

    private var database: DatabaseReference = FirebaseDatabase.getInstance().reference.child("games")

    private val _joinError = MutableLiveData<Boolean>()
    val joinError: LiveData<Boolean>
        get() = _joinError

    private val _gameFull = MutableLiveData<Boolean>()
    val gameFull: LiveData<Boolean>
        get() = _gameFull

    private val _player2 = MutableLiveData<String>()
    val player2: LiveData<String>
        get() = _player2

    private val _code = MutableLiveData<String>()
    val code: LiveData<String>
        get() = _code

    private val eventListener = object: ValueEventListener {
        override fun onCancelled(error: DatabaseError) {
        }

        override fun onDataChange(snapshot: DataSnapshot) {
            if (snapshot.exists()) {
                if (snapshot.child("player2").value==null){
                    val game = Game()
                    game.player1 = snapshot.child("player1").value.toString()
                    game.code = snapshot.child("code").value.toString()
                    game.player2 = player2.value!!
                    database.child(code.value!!).setValue(game)
                    detachListener()
                    _joinError.value = false
                    _gameFull.value = false
                } else {
                    _gameFull.value = true
                }
            } else {
                _joinError.value = true
            }
        }

    }

    fun detachListener(){
        database.child(code.value!!).removeEventListener(eventListener)
    }

    fun join(){
        database.child(code.value!!).addValueEventListener(eventListener)
    }

    fun setPlayer(name: String) {
        _player2.value = name
    }

    fun setCode(code: String) {
        _code.value = code
    }



}