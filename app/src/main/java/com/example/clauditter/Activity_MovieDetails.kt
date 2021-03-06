package com.example.clauditter

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.example.clauditter.adapters.IS_LOGED
import com.example.clauditter.adapters.PagerAdapter
import com.example.clauditter.adapters.USERNAME
import com.example.clauditter.ui.clases.Movie
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.movie_description.*

class Activity_MovieDetails : AppCompatActivity() {
    private lateinit var viewpager: ViewPager
    private var username: String? = ""
    private var flag = false
    private var flagExistInList = false
    private lateinit var movie: Movie
    private var dbMovieId:String=""
    private val dataModel: ViewModel_MovieDetails by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.movie_description)
        //Obteniendo valores pasados desde el fragment Home
        movie = intent.getParcelableExtra<Movie>(MOVIE_TRANSFER) as Movie
        dataModel.setMovie(movie)
        username = intent.getStringExtra(USERNAME)
        flag = intent.getBooleanExtra(IS_LOGED, false)

        Glide.with(this)
            .load(movie.poster_path)
            .into(img_vistaPelicula)
        //To implemet tabLayout
        viewpager = findViewById(R.id.viewPagerDetails)


        tabLayout_Categories.post(Runnable { tabLayout_Categories.setupWithViewPager(viewpager)
            tabLayout_Categories.getTabAt(0)?.setIcon(R.mipmap.information)
            tabLayout_Categories.getTabAt(1)?.setIcon(R.mipmap.cast)
            tabLayout_Categories.getTabAt(2)?.setIcon(R.mipmap.review)
            tabLayout_Categories.getTabAt(3)?.setIcon(R.drawable.ic_menu_slideshow)})

        //agregando controlador al viewpager
        val pagerAdapter = PagerAdapter(supportFragmentManager, movie)
        viewpager.adapter = pagerAdapter


        val toolbar: Toolbar = findViewById(R.id.toolbar_Movie)
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = movie.title
        if (flag) {
            btn_addRemoveFavorite.visibility = View.VISIBLE

            isAdded()
        } else {
            btn_addRemoveFavorite.visibility = View.GONE
        }

        btn_addRemoveFavorite.setOnClickListener(View.OnClickListener {
            addRemoveFavorite()
        })

    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    /*View Pager Methods*/
    override fun onBackPressed() {
        if (viewpager.currentItem == 0) {
            super.onBackPressed()
        } else {
            viewpager.currentItem = viewpager.currentItem - 1
        }
    }


    private fun setStyleRemove(){
        btn_addRemoveFavorite.text = "Remove from favorites"
        btn_addRemoveFavorite.setBackgroundColor(Color.RED)
    }
    private fun setStyleAdd(){
        btn_addRemoveFavorite.text = "Add to favorites"
        btn_addRemoveFavorite.setBackgroundColor(Color.GREEN)
    }

    private fun addRemoveFavorite() {
        if(flagExistInList){ //exist
            removeFromFavorites()
            setStyleAdd()
        }else{//not exist
            addToFavorites()
            setStyleRemove()
        }
        flagExistInList=!flagExistInList
        btn_addRemoveFavorite.setTextColor(Color.WHITE)
    }

    private fun addToFavorites() {
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


    private fun removeFromFavorites() {
        val db = FirebaseFirestore.getInstance()
        db.collection("favorites").document(dbMovieId)
            .delete()
            .addOnSuccessListener { Toast.makeText(
                this,
                "${movie.title} deleted from  favorites",
                Toast.LENGTH_SHORT
            ).show() }
            .addOnFailureListener { Toast.makeText(
                this,
                "There was an error :(",
                Toast.LENGTH_SHORT
            ).show() }
    }

//Verifies if a movie is already added to a favorites list
    private fun isAdded() {
        val db = FirebaseFirestore.getInstance()
        val filter = db.collection("favorites")
        filter.whereEqualTo("username", username).whereEqualTo("movieTitle", movie.title)
            .whereEqualTo("movieId",movie.id).addSnapshotListener { snapshots, e ->
            if (e != null) {
                //error
                return@addSnapshotListener
            }
            val documents = snapshots?.documents
                 if(documents!=null&&documents.size>=1){
                    setStyleRemove()
                    dbMovieId=documents[0].id
                    flagExistInList=true
                }else{
                    setStyleAdd()
                    flagExistInList=false
                }
        }
    }
}//end of class