package com.example.clauditter

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.FragmentActivity
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.example.clauditter.adapters.IS_LOGED
import com.example.clauditter.adapters.PagerAdapter
import com.example.clauditter.adapters.USERNAME
import com.example.clauditter.ui.clases.Favorite
import com.example.clauditter.ui.clases.Movie
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.fragment_favorites.*
import kotlinx.android.synthetic.main.movie_description.*


private const val TAG = "DETAILS"

class MovieDetailsActivity : FragmentActivity() {
    private lateinit var viewpager: ViewPager
    private var username: String? = ""
    private var flag = false
    private var flagExistInList = false
    private lateinit var movie: Movie
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.movie_description)
        movie = intent.getParcelableExtra<Movie>(MOVIE_TRANSFER) as Movie
        Glide.with(this)
            .load(movie.poster_path)
            .into(img_vistaPelicula)
        viewpager = findViewById(R.id.viewPagerDetails)
        val pagerAdapter = PagerAdapter(supportFragmentManager, movie)
        viewpager.adapter = pagerAdapter

        username = intent.getStringExtra(USERNAME)
        flag = intent.getBooleanExtra(IS_LOGED, false)

        if (flag) {
            btn_addRemoveFavorite.visibility = View.VISIBLE
            flagExistInList=isAdded()
            if(flagExistInList){
                setStyleRemove()
            }else{
                setStyleAdd()
            }
        } else {
            btn_addRemoveFavorite.visibility = View.GONE
        }
        //PROJECT ID: toyproject-pp-fase1 TODO
        btn_addRemoveFavorite.setOnClickListener(View.OnClickListener {
            addRemoveFavorite()

        })


    }

    override fun onBackPressed() {
        if (viewpager.currentItem == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed()
        } else {
            // Otherwise, select the previous step.
            viewpager.currentItem = viewpager.currentItem - 1
        }
    }
    fun setStyleRemove(){
        btn_addRemoveFavorite.text = "Remove from favorites"
        btn_addRemoveFavorite.setBackgroundColor(Color.RED)
    }
    fun setStyleAdd(){
        btn_addRemoveFavorite.text = "Add to favorites"
        btn_addRemoveFavorite.setBackgroundColor(Color.GREEN)
    }

    fun addRemoveFavorite() {
        if (flagExistInList) {//existe
            setStyleAdd()
            removeFromFavorites()
        } else {//no existe
            addToFavorites()
            setStyleRemove()
        }
        flagExistInList=!flagExistInList
        btn_addRemoveFavorite.setTextColor(Color.WHITE)
    }

    fun addToFavorites() {
        val favorite = hashMapOf(
            "username" to username,
            "movieId" to movie.id,
            "movieTitle" to movie.title,
            "photo" to movie.backdrop_path
        )
        val db = FirebaseFirestore.getInstance()
        db.collection("favorites")
            .add(favorite)
            .addOnSuccessListener {
                Toast.makeText(
                    this,
                    "${movie.title} added to favorite",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnFailureListener {
                Toast.makeText(
                    this,
                    "There was an error :(",
                    Toast.LENGTH_SHORT
                ).show()
            }

    }


    fun removeFromFavorites() {

    }


    fun isAdded():Boolean {
        var resp=false
        val db = FirebaseFirestore.getInstance()
        val filter=db.collection("favorites")
            .whereEqualTo("username", username)
            .whereEqualTo("movieTitle", movie.title)
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    //error
                    return@addSnapshotListener
                }
                val documents = snapshots?.documents
                resp = !(documents == null || documents.size <= 0)
            }

        return resp

    }




}//end of class