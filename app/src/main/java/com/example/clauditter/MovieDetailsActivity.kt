package com.example.clauditter
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.example.clauditter.ui.clases.Movie
import com.example.clauditter.adapters.PagerAdapter
import kotlinx.android.synthetic.main.movie_description.*
import androidx.viewpager.widget.ViewPager

private const val TAG="DETAILS"
class MovieDetailsActivity : FragmentActivity(){
    private lateinit var viewpager: ViewPager

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