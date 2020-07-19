package com.example.guessit.model
import com.google.firebase.database.Exclude
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
) {

    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "player1" to player1,
            "player2" to player2,
            "code" to code,
            "player1Score" to player1Score,
            "player2Score" to player2Score,
            "player2Turn" to isPlayer2Turn,
            "gameFinished" to isGameFinished
        )
    }

}