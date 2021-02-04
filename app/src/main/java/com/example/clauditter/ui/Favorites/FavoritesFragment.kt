package com.example.clauditter.ui.Favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.clauditter.ViewModelLogIn
import com.example.clauditter.R
import com.example.clauditter.adapters.FavoritesAdapter
import com.example.clauditter.ui.clases.Favorite
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_favorites.*
import okhttp3.OkHttpClient


class FavoritesFragment : Fragment() {

    private lateinit var slideshowViewModel: FavoritesViewModel
    private val logInModel: ViewModelLogIn by activityViewModels()

    /**RECYCLER ADAPTER */
    private var favoritesAdapter: FavoritesAdapter = FavoritesAdapter(ArrayList())
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
                if (logInModel.favoriteListIsLoaded()) {//downloading new data
                    Toast.makeText(activity, "downloading new data", Toast.LENGTH_LONG).show()
                    getFavorites()
                }
            } else {
                lbl_warningFavorites.visibility = View.VISIBLE
                recycler_favorites.visibility = View.GONE
                lbl_favoritesMessage.visibility = View.GONE
            }
        })

        logInModel.user.observe(viewLifecycleOwner, Observer {
            lbl_favoritesMessage.text = "$it´ s Favorites List"
            getFavorites() //new user, new favorites
        })


        logInModel.favoritesList.observe(viewLifecycleOwner, Observer {
            favoritesAdapter.loadNewData(it)
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
                                document["photo"].toString(),
                                        document.id
                            )
                        )
                    }
                    logInModel.loadFavoriteList(favorites)
                    //favoritesAdapter.loadNewData(favorites)
                }
            }
    }
}//End Of Class