package com.example.guessit.ui.waiting

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.guessit.model.Game
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.*
import java.lang.Exception
import java.util.*


class WaitingViewModel(playerName: String): ViewModel() {

    private val database: DatabaseReference= FirebaseDatabase.getInstance().reference

    private val _player1 = MutableLiveData<String>()
    val player1: LiveData<String>
        get() = _player1

    private val _code = MutableLiveData<String>()
    val code: LiveData<String>
        get() = _code

    private val _roomCreated = MutableLiveData<Boolean>()
    val roomCreated: LiveData<Boolean>
        get() = _roomCreated

    init {
        _player1.value = playerName
        createRoom()
    }

    private fun createRoom(){
        Log.e(TAG, player1.value!!)
        _code.value = UUID.randomUUID().toString().substring(0, 6)
        val game = Game(player1.value!!, null, code.value!!)
        Log.i("MAL", "123")
        database.child("games").child(code.value!!).setValue(game)
    }

    fun deleteGame(){
        database.child("games").child(code.value!!).removeValue()
    }

}