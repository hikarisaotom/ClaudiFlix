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
    private val mutableCastLoaded= MutableLiveData<Boolean>()
    private val mutableTrailersLoaded= MutableLiveData<Boolean>()
    private val mutableReviewsLoaded= MutableLiveData<Boolean>()

    init {
        mutableCast.value= ArrayList()
       mutableReviews .value= ArrayList()
        mutableTrailers.value= ArrayList()
        mutableCastLoaded.value=false
        mutableTrailersLoaded.value=false
        mutableReviewsLoaded.value=false
    }
    val cast: LiveData<ArrayList<Person>> get() = mutableCast
    val reviews: LiveData<ArrayList<Review>> get() = mutableReviews
    val trailers: LiveData<ArrayList<Trailer>> get() = mutableTrailers
    val movie: LiveData<Movie> get() = mutableMovie
    val castLoaded : LiveData<Boolean> get() = mutableCastLoaded
    val trailersLoaded: LiveData<Boolean> get() =mutableTrailersLoaded
    val reviewsLoaded: LiveData<Boolean> get() =mutableReviewsLoaded

    fun loadNewCast(newData:ArrayList<Person>){
        mutableCast.value=newData
        mutableCastLoaded.value=true
    }

    fun loadNewReviews(newData:ArrayList<Review>){
        mutableReviews.value=newData
        mutableReviewsLoaded.value=true
    }
    fun loadNewTrailers(newData:ArrayList<Trailer>){
        mutableTrailers.value=newData
        mutableTrailersLoaded.value=true
    }

    fun setMovie(newMovie:Movie){
        mutableMovie.value=newMovie
    }




}//end of class