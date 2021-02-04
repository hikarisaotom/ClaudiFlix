package com.example.clauditter.ui.Favorites

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.clauditter.LogViewModel
import com.example.clauditter.R
import com.example.clauditter.adapters.CategoriasHomeAdapter
import com.example.clauditter.adapters.FavoritesAdapter
import com.example.clauditter.ui.clases.Favorite
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_favorites.*


class FavoritesFragment : Fragment() {

    private lateinit var slideshowViewModel: FavoritesViewModel
    private val logInModel: LogViewModel by activityViewModels()
    /**RECYCLER ADAPTER */
    private  var favoritesAdapter: FavoritesAdapter=FavoritesAdapter(ArrayList())
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        slideshowViewModel =
            ViewModelProvider(this).get(FavoritesViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_favorites, container, false)
        logInModel.flag.observe(viewLifecycleOwner, Observer {
            if (it) {
                lbl_warningFavorites.visibility = View.GONE
                recycler_favorites.visibility = View.VISIBLE
                lbl_favoritesMessage.visibility = View.VISIBLE
                getFavorites()
            } else {
                lbl_warningFavorites.visibility = View.VISIBLE
                recycler_favorites.visibility = View.GONE
                lbl_favoritesMessage.visibility = View.GONE
            }
        })

        logInModel.user.observe(viewLifecycleOwner, Observer {
                lbl_favoritesMessage.text= "$it´ s Favorite List"
        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler_favorites.layoutManager = LinearLayoutManager(activity)
        recycler_favorites.adapter = favoritesAdapter
    }

    fun getFavorites() {
        val favorites = ArrayList<Favorite>()
        val db = FirebaseFirestore.getInstance()
        db.collection("favorites")
            .whereEqualTo("username", logInModel.user.value.toString())
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    //error
                    return@addSnapshotListener
                }
                val documents = snapshots?.documents
                if (documents == null || documents.size <= 0) {
                    recycler_favorites.visibility = View.GONE
                    lbl_warningFavorites.visibility = View.VISIBLE
                    lbl_warningFavorites.text = "You don`t have any movie added yet"
                } else {
                    documents?.forEach { document ->
                        favorites.add(
                            Favorite(
                                document["movieId"].toString().toInt(),
                                document["movieTitle"].toString(),
                                document["photo"].toString()
                            )
                        )
                    }
                    favoritesAdapter.loadNewData(favorites)
                }
            }
    }
}//End Of Class