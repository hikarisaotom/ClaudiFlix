package com.example.clauditter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.clauditter.ui.clases.*

class ViewModel_MovieDetails : ViewModel() {
    /*Live Data esta optimizado para uso en ciclos de vida, mutablelive data permite que cambien*/
    private val mutableCast = MutableLiveData<ArrayList<Person>>()
    private val mutableReviews = MutableLiveData<ArrayList<Review>>()
    private val mutableTrailers= MutableLiveData<ArrayList<Trailer>>()
    private val mutableMovie= MutableLiveData<Movie>()

    init {
        mutableCast.value= ArrayList()
       mutableReviews .value= ArrayList()
        mutableTrailers.value= ArrayList()
    }
    val cast: LiveData<ArrayList<Person>> get() = mutableCast
    val reviews: LiveData<ArrayList<Review>> get() = mutableReviews
    val trailers: LiveData<ArrayList<Trailer>> get() = mutableTrailers
    val movie: LiveData<Movie> get() = mutableMovie

    fun loadNewCast(newData:ArrayList<Person>){
        mutableCast.value=newData
    }

    fun loadNewReviews(newData:ArrayList<Review>){
        mutableReviews.value=newData
    }
    fun loadNewTrailers(newData:ArrayList<Trailer>){
        mutableTrailers.value=newData
    }

    fun setMovie(newMovie:Movie){
        mutableMovie.value=newMovie
    }




}//end of class