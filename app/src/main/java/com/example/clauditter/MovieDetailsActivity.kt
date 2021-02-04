package com.example.clauditter
import android.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.FragmentActivity
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.example.clauditter.adapters.PagerAdapter
import com.example.clauditter.adapters.USERNAME
import com.example.clauditter.adapters.IS_LOGED
import com.example.clauditter.ui.clases.Movie
import kotlinx.android.synthetic.main.movie_description.*


private const val TAG="DETAILS"
class MovieDetailsActivity : FragmentActivity(){
    private lateinit var viewpager: ViewPager
    private val LogViewModel: LogViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.movie_description)
        val movie=intent.getParcelableExtra<Movie>(MOVIE_TRANSFER) as Movie
        Glide.with(this)
            .load(movie.poster_path)
            .into(img_vistaPelicula)
        viewpager = findViewById(R.id.viewPagerDetails)
        val pagerAdapter = PagerAdapter(supportFragmentManager,movie)
        viewpager.adapter = pagerAdapter

        val username=intent.getStringExtra(USERNAME)
        val flag=intent.getBooleanExtra(IS_LOGED,false)

        if(flag){
            btn_addRemoveFavorite.visibility=View.VISIBLE
        }else{
            btn_addRemoveFavorite.visibility=View.GONE
            //todo verificar que este en la lista de los favoritos
        }


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



}//end of class