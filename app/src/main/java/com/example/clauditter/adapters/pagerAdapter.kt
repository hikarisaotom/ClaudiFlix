package com.example.clauditter.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.viewpager.widget.PagerAdapter
import com.example.clauditter.R

class PagerAdapter (private val context: Context) : PagerAdapter() {

    var layoutes = intArrayOf(R.layout.actores, R.layout.fragment_actores, R.layout.actores)
    override fun getCount(): Int {
        return layoutes.size
    }

    override fun isViewFromObject(
        view: View,
        `object`: Any
    ): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val one: View = layoutInflater.inflate(R.layout.actores, container, false)
        val two: View = layoutInflater.inflate(R.layout.fragment_actores, container, false)
        val three: View = layoutInflater.inflate(R.layout.actores, container, false)
        val viewarr = arrayOf(one, two, three)
        container.addView(viewarr[position])
        return viewarr[position]
    }

    override fun destroyItem(
        container: ViewGroup,
        position: Int,
        `object`: Any
    ) {
        container.removeViewAt(position)
    }

}