package com.example.guessit.ui.waiting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class WaitingViewModelFactory(private val playerName: String):  ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(WaitingViewModel::class.java)) {
            return WaitingViewModel(playerName) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}