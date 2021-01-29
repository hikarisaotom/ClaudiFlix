package com.example.clauditter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class MovieDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.movie_description)
        setSupportActionBar(findViewById(R.id.toolbar))
    }
}