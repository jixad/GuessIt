package com.example.guessit.ui.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class GameViewModelFactory(private var isCreator: Boolean = false,
                           private var isJoiner: Boolean = false,
                           private var code: String?): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameViewModel::class.java)){
            return GameViewModel(isCreator, isJoiner, code) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}