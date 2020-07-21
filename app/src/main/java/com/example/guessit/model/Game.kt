package com.example.guessit.model
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class Game(
    var player1: String = "",
    var player2: String? = "",
    var code: String = "",
    var player1Score: Int? = 0,
    var player2Score: Int? = 0,
    var isPlayer2Turn: Boolean? = null,
    var isGameFinished: Boolean? = null
)