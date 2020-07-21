package com.example.guessit.ui.finish

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.guessit.model.Game
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue

class FinishViewModel(private var code: String): ViewModel() {

    private var database: DatabaseReference = FirebaseDatabase.getInstance().reference.child("games")
    private var winnerListener: ValueEventListener

    private val _winner = MutableLiveData<String>()
    val winner: LiveData<String>
        get() = _winner


    init {
        winnerListener = object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    val game = snapshot.getValue<Game>()
                    _winner.value = when {
                        game?.player1Score!!> game.player2Score!! -> game.player1
                        game.player1Score!!< game.player2Score!! -> game.player2
                        else -> "DRAW"
                    }
                }
            }

        }
        database.child(code).addListenerForSingleValueEvent(winnerListener)
    }

}