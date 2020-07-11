package com.example.guessit.model
import kotlinx.serialization.Serializable

@Serializable
class Game {

    var player1: String = ""
    var player2: String = ""
    var code: String = ""
    var player1Score: Int = 0
    var player2Score: Int = 0
    var isPlayer2Turn: Boolean? = null
    var isGameFinished: Boolean? = null

    constructor()

    constructor(player1: String, player2: String, code: String, player1Score: Int,
                player2Score: Int) {
        this.player1 = player1
        this.player2 = player2
        this.code = code
        this.player1Score = player1Score
        this.player2Score = player2Score
        //this.isPlayer2Turn = isPlayer2Turn
        //this.isGameFinished = isGameFinished
    }
}