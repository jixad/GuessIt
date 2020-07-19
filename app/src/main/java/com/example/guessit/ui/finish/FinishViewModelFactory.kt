package com.example.guessit.ui.finish

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class FinishViewModelFactory(private var code: String): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FinishViewModel::class.java)){
            return FinishViewModel(code) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}