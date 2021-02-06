package com.example.clauditter.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.clauditter.Fragment_Information
import com.example.clauditter.Fragment_Trailers
import com.example.clauditter.Fragment_cast
import com.example.clauditter.Fragment_reviews
import com.example.clauditter.ui.clases.Movie
import kotlinx.android.synthetic.main.movie_description.*
import kotlinx.android.synthetic.main.movie_description.*

class PagerAdapter(fm: FragmentManager,val movie:Movie) : FragmentStatePagerAdapter(fm) {
    override fun getCount(): Int =4 //I only have 3 fragments to show

    override fun getItem(position: Int): Fragment {
        var fragmentToShow:Fragment?=null
        fragmentToShow = when(position){
            0 ->{ Fragment_Information() }
            1 ->{ Fragment_cast() }
            2 ->{ Fragment_reviews() }
            else ->{ Fragment_Trailers() }
        }

        return fragmentToShow
    }
}