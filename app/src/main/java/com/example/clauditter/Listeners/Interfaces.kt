package com.example.clauditter.Listeners

import android.view.View

//interface to implement onHomeFragment
interface OnRecyclerClickListener {
    fun onItemClick(view: View, position: Int)
    fun onItemLongClick(view: View, position: Int)
}

interface FragmentToActivity {
    fun communicate(comm: String?)
}




