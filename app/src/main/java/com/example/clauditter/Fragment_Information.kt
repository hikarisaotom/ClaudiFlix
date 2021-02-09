package com.example.clauditter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.clauditter.ui.clases.Movie
import kotlinx.android.synthetic.main.fragment_information.*



class Fragment_Information : Fragment() {
    private val dataModel: ViewModel_MovieDetails by activityViewModels()
    private var movie:Movie?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        movie=dataModel.movie.value
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_information, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lbl_movie_title.text=movie?.title
        lbl_release.text=movie?.release_date
        lbl_duration.text=movie?.vote_average.toString()
        lbl_puntuation.text = movie?.popularity.toString()
        lbl_sinopsis.text=movie?.overview
        lbl_lenguage.text=movie?.original_language

        lbl_icon_duration.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.time, 0, 0, 0);
        lbl_icon_puntuation.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.puntuation, 0, 0, 0);
        lbl_icon_relese.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.release, 0, 0, 0);
        lbl_icon_sinopsis.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.information, 0, 0, 0);
        lbl_icon_lenguage.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.language, 0, 0, 0);
        lbl_icon_sinopsis.text=" Sinopsis"

    }

}