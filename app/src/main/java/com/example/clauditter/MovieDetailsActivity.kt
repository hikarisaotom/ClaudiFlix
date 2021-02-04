package com.example.clauditter

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
import com.example.clauditter.ui.clases.Movie
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.movie_description.*


private const val TAG = "DETAILS"

class MovieDetailsActivity : FragmentActivity() {
    private lateinit var viewpager: ViewPager
    private val LogViewModel: LogViewModel by viewModels()
    private var username: String? = ""
    private var flag = false
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
        } else {
            btn_addRemoveFavorite.visibility = View.GONE
            //todo verificar que este en la lista de los favoritos
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


    fun addRemoveFavorite() {
        addToFavorites()
       /* if (btn_addRemoveFavorite.text.equals(R.string.addFavorite)) {
            btn_addRemoveFavorite.text = R.string.removeFavorite.toString()
            btn_addRemoveFavorite.setTextColor(Color.RED)
            addToFavorites()
        } else {
            btn_addRemoveFavorite.text = R.string.addFavorite.toString()
            btn_addRemoveFavorite.setTextColor(Color.parseColor("#9E9E9E"))
            //todo eliminar de la bdd de firebase
        }
        btn_addRemoveFavorite.setTextColor(Color.WHITE)*/
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





}//end of class