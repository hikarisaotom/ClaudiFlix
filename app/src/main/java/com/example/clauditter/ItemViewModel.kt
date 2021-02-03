package com.example.clauditter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LogViewModel : ViewModel() {
    /*Live Data esta optimizado para uso en ciclos de vida, mutablelive data permite que cambien*/
    private val mutableLogIn = MutableLiveData<Boolean>()
    private val mutableUser = MutableLiveData<String>()
    init {
        mutableLogIn.value=false
        mutableUser.value=""
    }
    val flag: LiveData<Boolean> get() = mutableLogIn
    val user: LiveData<String> get() = mutableUser

    fun setFlag(flag: Boolean) {
        mutableLogIn.value = flag
    }
    fun setUser(username: String) {
        mutableUser.value = username
    }
}