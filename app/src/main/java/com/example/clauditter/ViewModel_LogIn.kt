package com.example.clauditter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.clauditter.ui.clases.Favorite
import com.example.clauditter.ui.clases.Movie
import com.example.clauditter.ui.clases.MovieList
import com.example.clauditter.ui.home.listToDomwload

class ViewModel_LogIn : ViewModel() {
    /*Live Data esta optimizado para uso en ciclos de vida, mutablelive data permite que cambien*/
    private val mutableLogIn = MutableLiveData<Boolean>()
    private val mutableUser = MutableLiveData<String>()
    private val mutableMovieList = MutableLiveData<ArrayList<MovieList>>()
    private val mutableFavoriteList = MutableLiveData<ArrayList<Favorite>>()
    private val mutableIsFull=MutableLiveData<Boolean>()

    init {
        mutableLogIn.value=false
        mutableUser.value=""
        mutableMovieList.value= ArrayList()
        mutableFavoriteList.value= ArrayList()
        mutableIsFull.value=false
    }
    val flag: LiveData<Boolean> get() = mutableLogIn
    val user: LiveData<String> get() = mutableUser
    val movieList: LiveData<ArrayList<MovieList>> get() = mutableMovieList
    val favoritesList: LiveData<ArrayList<Favorite>> get() = mutableFavoriteList
    val isFull:LiveData<Boolean> get() = mutableIsFull

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
        mutableIsFull.value=(newList.size>= listToDomwload.size)
    }


    fun favoriteListIsLoaded():Boolean{
        return mutableFavoriteList.value!!.isEmpty()
    }

    fun loadFavoriteList(newList: ArrayList<Favorite>){
        mutableFavoriteList.value=newList

    }

    fun getMovie(movieid:Int?):Movie?{
        mutableMovieList.value?.forEach { movieList ->
            movieList.moviesToShow.forEach {
                if(it.id==movieid){
                    return it
                }
            }
        }
        return null
    }

    fun deleteMovie(movieid:Int?){
        mutableFavoriteList.value?.forEach {
            if(it.movieid==movieid){
                mutableFavoriteList.value?.remove(it)
            }
        }

    }



}//end of class