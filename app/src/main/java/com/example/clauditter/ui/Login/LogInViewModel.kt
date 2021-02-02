package com.example.clauditter.ui.Login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LogInViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "LOG IN TO THE APP"
    }
    val text: LiveData<String> = _text
}