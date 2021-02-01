package com.example.clauditter.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.clauditter.Fragment_Information
import com.example.clauditter.Fragment_cast
import com.example.clauditter.Fragment_reviews
import com.example.clauditter.ui.clases.Movie


class PagerAdapter(fm: FragmentManager,val movie:Movie) : FragmentStatePagerAdapter(fm) {
    override fun getCount(): Int =3 //I only have 3 fragments to show

    override fun getItem(position: Int): Fragment {
        val bundle = Bundle()
        var fragmentToShow:Fragment?=null
        bundle.putParcelable(MOVIE_TRANSFER,movie)
        when(position){
            0 ->{fragmentToShow= Fragment_Information()}
            1 ->{fragmentToShow= Fragment_cast()}
            2 ->{fragmentToShow= Fragment_reviews()}
            else ->{fragmentToShow= Fragment_Information()}
        }
        fragmentToShow.setArguments(bundle)
        return fragmentToShow
    }
}