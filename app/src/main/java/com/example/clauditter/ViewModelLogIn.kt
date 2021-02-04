package com.example.clauditter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.clauditter.ui.clases.MovieList

class ViewModelLogIn : ViewModel() {
    /*Live Data esta optimizado para uso en ciclos de vida, mutablelive data permite que cambien*/
    private val mutableLogIn = MutableLiveData<Boolean>()
    private val mutableUser = MutableLiveData<String>()
    private val mutableMovieList = MutableLiveData<ArrayList<MovieList>>()
    init {
        mutableLogIn.value=false
        mutableUser.value=""
        mutableMovieList.value= ArrayList()
    }
    val flag: LiveData<Boolean> get() = mutableLogIn
    val user: LiveData<String> get() = mutableUser
    val CategoriesList: LiveData<ArrayList<MovieList>> get() = mutableMovieList

    fun setFlag(flag: Boolean) {
        mutableLogIn.value = flag
    }
    fun setUser(username: String) {
        mutableUser.value = username
    }

    fun movieListIsLoaded():Boolean{
        return mutableMovieList.value!!.isEmpty()
    }

    fun loadMovieList(newList: ArrayList<MovieList>){
        mutableMovieList.value=newList
    }
}