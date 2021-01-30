package com.example.clauditter

import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.clauditter.adapters.CategoriasHomeAdapter
import com.example.clauditter.ui.clases.Movie
import com.example.clauditter.adapters.CastAdapter
import com.example.clauditter.ui.clases.Person
import com.example.clauditter.adapters.PagerAdapter
import com.google.gson.Gson
import kotlinx.android.synthetic.main.movie_description.*
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

import androidx.viewpager.widget.ViewPager
import com.example.clauditter.ui.home.HomeFragment

private const val NUM_PAGES = 5
private const val TAG="DETAILS"
class MovieDetailsActivity : FragmentActivity(){
/*
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.movie_description)
        setSupportActionBar(findViewById(R.id.toolbar))
        //Movie transfer is declared into MoviePreviewAdapter
           val movie=intent.getParcelableExtra<Movie>(MOVIE_TRANSFER) as Movie
        //Corregir el link de la imagen
        Glide.with(this)
            .load(movie.poster_path)
            .into(img_vistaPelicula)
            movieId=movie.id

        val viewPager:ViewPager = findViewById(R.id.viewPager)
        val mViewPagerAdapter = PagerAdapter(this)
        viewPager.adapter = mViewPagerAdapter

    }*/


    private lateinit var mPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.movie_description)
        // Instantiate a ViewPager and a PagerAdapter.
        mPager = findViewById(R.id.viewPagerDetails)

        // The pager adapter, which provides the pages to the view pager widget.
        val pagerAdapter = ScreenSlidePagerAdapter(supportFragmentManager)
        mPager.adapter = pagerAdapter
        //Movie transfer is declared into MoviePreviewAdapter
        val movie=intent.getParcelableExtra<Movie>(MOVIE_TRANSFER) as Movie
        //Corregir el link de la imagen
        Glide.with(this)
            .load(movie.poster_path)
            .into(img_vistaPelicula)
        val movieId=movie.id
    }

    override fun onBackPressed() {
        if (mPager.currentItem == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed()
        } else {
            // Otherwise, select the previous step.
            mPager.currentItem = mPager.currentItem - 1
        }
    }


    private inner class ScreenSlidePagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
        override fun getCount(): Int = NUM_PAGES

        override fun getItem(position: Int): Fragment {
            if (position==0){
                return Fragment_cast()
            }else{
                return HomeFragment()
            }
        }
    }


}//end of class