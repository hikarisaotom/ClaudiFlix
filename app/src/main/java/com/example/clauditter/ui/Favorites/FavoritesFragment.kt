package com.example.clauditter.ui.Favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.clauditter.LogViewModel
import com.example.clauditter.R

class FavoritesFragment : Fragment() {

    private lateinit var slideshowViewModel: FavoritesViewModel
    private val logInModel: LogViewModel by activityViewModels()
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        slideshowViewModel =
                ViewModelProvider(this).get(FavoritesViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_favorites, container, false)
        val textView: TextView = root.findViewById(R.id.text_slideshow)
        slideshowViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        logInModel.flag.observe(viewLifecycleOwner, Observer {
            if(it){
                textView.visibility=View.VISIBLE
            }else{//Invisible: no s emuestra pero ocupa espacio en la pantalla, con gone se va todo
                textView.visibility=View.GONE
            }
        })
        return root
    }
}