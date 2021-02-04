package com.example.clauditter

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

const val MOVIE_TRANSFER="MOVIE_TRANSFER"
@SuppressLint("Registered")
open class BaseActivity:AppCompatActivity() {
    private val TAG="BaseActivity"

internal fun activateToolbar(enableHome:Boolean){

    var toolbar=findViewById<View>(R.id.toolbar) as Toolbar
    //establecemos que toolbarusar
    setSupportActionBar(toolbar)
    //mostramos u ocultamos la tollbar de retroceso a home
    supportActionBar?.setDisplayHomeAsUpEnabled(enableHome)
}



}