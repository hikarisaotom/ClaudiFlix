package com.example.clauditter.ui.Favorites

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer

import androidx.recyclerview.widget.LinearLayoutManager
import com.example.clauditter.Activity_MovieDetails
import com.example.clauditter.Listeners.OnRecyclerClickListener
import com.example.clauditter.Listeners.RecyclerItemsListeners
import com.example.clauditter.ViewModel_LogIn
import com.example.clauditter.R
import com.example.clauditter.adapters.FavoritesAdapter
import com.example.clauditter.adapters.IS_LOGED
import com.example.clauditter.adapters.MOVIE_TRANSFER
import com.example.clauditter.adapters.USERNAME
import com.example.clauditter.ui.clases.Favorite
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment__trailers.*
import kotlinx.android.synthetic.main.fragment_favorites.*



class FavoritesFragment : Fragment(),
    OnRecyclerClickListener{

    private val logInModel: ViewModel_LogIn by activityViewModels()
    /**RECYCLER ADAPTER */
    private var favoritesAdapter: FavoritesAdapter = FavoritesAdapter(ArrayList())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_favorites, container, false)
       //show/hide elements if the user is logged in
        logInModel.flag.observe(viewLifecycleOwner, Observer {
            if (it) {
                lbl_warningFavorites.visibility = View.GONE
                recycler_favorites.visibility = View.VISIBLE
                lbl_favoritesMessage.visibility = View.VISIBLE
               // if (logInModel.favoriteListIsLoaded()) {//downloading new data,
                /*This is commented because if not, the list wouldn`t be always updated*/
                    Toast.makeText(activity, "downloading new data", Toast.LENGTH_LONG).show()
                    getFavorites()
            //    }
            } else {
                lbl_warningFavorites.visibility = View.VISIBLE
                recycler_favorites.visibility = View.GONE
                lbl_favoritesMessage.visibility = View.GONE
            }
        })

        logInModel.user.observe(viewLifecycleOwner, Observer {
            lbl_favoritesMessage.text = "$it´ s Favorites List"
           // getFavorites() //new user, new favorites, this is not necessary anymore because the if above is comented
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
        recycler_favorites.addOnItemTouchListener(RecyclerItemsListeners(view.context,recycler_favorites,this))
    }

    private fun getFavorites() {
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
                    val favorites = ArrayList<Favorite>()
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

    /**Listeners for the recyclerView Items*/
    override fun onItemClick(view: View, position: Int) {
        val favMovie=favoritesAdapter.getFavorite(position)
        val movie=logInModel.getMovie(favMovie?.movieid)
        if(movie!=null){
            Toast.makeText(view.context ," ${movie.title}", Toast.LENGTH_SHORT).show()
            val intent= Intent(view.context, Activity_MovieDetails::class.java)
            intent.putExtra(MOVIE_TRANSFER,movie)
            intent.putExtra(USERNAME,logInModel.user.value)
            intent.putExtra(IS_LOGED,logInModel.flag.value)
            view.context.startActivity(intent)
        }
    }

    override fun onItemLongClick(view: View, position: Int) {
            //todo implement confirmation dialog
        val favMovie=favoritesAdapter.getFavorite(position)
               if(favMovie!=null){
                   val db = FirebaseFirestore.getInstance()
                   db.collection("favorites").document(favMovie.id)
                       .delete()
                       .addOnSuccessListener { Toast.makeText(
                           view.context,
                           "${favMovie.movieTitle} deleted from  favorites",
                           Toast.LENGTH_SHORT
                       ).show()

                           }
                       .addOnFailureListener { Toast.makeText(
                           view.context,
                           "There was an error :(",
                           Toast.LENGTH_SHORT
                       ).show() }


               }
    }
}//End Of Class