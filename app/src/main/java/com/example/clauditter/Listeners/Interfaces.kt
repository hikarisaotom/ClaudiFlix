package com.example.clauditter.Listeners

import android.os.Bundle
import android.view.View

//interface to implement onHomeFragment
interface OnRecyclerClickListener {
    fun onItemClick(view: View, position: Int)
    fun onItemLongClick(view: View, position: Int)
}


interface OnDownloadComplete {
    fun parseJson (datos:Bundle)
}







