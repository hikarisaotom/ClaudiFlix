package com.example.clauditter.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.clauditter.Fragment_Information
import com.example.clauditter.Fragment_cast
import com.example.clauditter.ui.clases.Movie
import com.example.clauditter.ui.home.HomeFragment


class PagerAdapter(fm: FragmentManager,val movie:Movie) : FragmentStatePagerAdapter(fm) {
    override fun getCount(): Int =3 //I only have 3 fragments to show

    override fun getItem(position: Int): Fragment {
        val bundle = Bundle()
        var fragmentToShow:Fragment?=null
        bundle.putParcelable(MOVIE_TRANSFER,movie)
        if (position==0){
            fragmentToShow= Fragment_Information()
        }else{
            fragmentToShow= Fragment_cast()
        }
        fragmentToShow.setArguments(bundle)
        return fragmentToShow
    }
}