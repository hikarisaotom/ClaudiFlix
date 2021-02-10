package com.example.clauditter.ui.Favorites

import android.app.AlertDialog
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
import com.example.clauditter.Listeners.OnDownloadComplete
import com.example.clauditter.Listeners.OnRecyclerClickListener
import com.example.clauditter.Listeners.RecyclerItemsListeners
import com.example.clauditter.MainActivity
import com.example.clauditter.ViewModel_LogIn
import com.example.clauditter.R
import com.example.clauditter.adapters.FavoritesAdapter
import com.example.clauditter.adapters.IS_LOGED
import com.example.clauditter.adapters.MOVIE_TRANSFER
import com.example.clauditter.adapters.USERNAME
import com.example.clauditter.ui.clases.Favorite
import com.example.clauditter.ui.clases.Movie
import com.example.clauditter.ui.home.JSON
import com.example.clauditter.ui.home.URL_DOWNLOAD
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_favorites.*
import org.json.JSONObject


class FavoritesFragment : Fragment(),
    OnRecyclerClickListener, OnDownloadComplete {

    private val logInModel: ViewModel_LogIn by activityViewModels()

    /**RECYCLER ADAPTER */
    private var favoritesAdapter: FavoritesAdapter = FavoritesAdapter(ArrayList())
    private var viewToShow:View?=null
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
                val films = 0x1F39E
                val movie = 0x1F3A5
                val emoji = String(Character.toChars(films))
                val emoji2 = String(Character.toChars(movie))
                lbl_warningFavorites.text= getString(R.string.LoginTosee)+" $emoji $emoji2"

                recycler_favorites.visibility = View.GONE
                lbl_favoritesMessage.visibility = View.GONE
            }
        })

        logInModel.user.observe(viewLifecycleOwner, Observer {
            val films = 0x1F39E
            val emoji = String(Character.toChars(films))
            lbl_favoritesMessage.text = "$it´ s Favorites List $emoji"
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
        recycler_favorites.addOnItemTouchListener(
            RecyclerItemsListeners(
                view.context,
                recycler_favorites,
                this
            )
        )
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
        val funciones = MainActivity()
        val favMovie = favoritesAdapter.getFavorite(position)
        viewToShow=view
        val url = funciones.createUrl("movie/${favMovie!!.movieid}",null)
        val datos = Bundle()
        datos.putString(URL_DOWNLOAD, url)
        funciones.downloadData(datos, this)
    }

    override fun onItemLongClick(view: View, position: Int) {
        val favMovie = favoritesAdapter.getFavorite(position)
        if (favMovie != null) {
            val builder = AlertDialog.Builder(activity)
            builder.setTitle("Delete From Favorite")
            builder.setMessage("Are you sure you want to delete ${favMovie.movieTitle}")
            builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                val db = FirebaseFirestore.getInstance()
                db.collection("favorites").document(favMovie.id)
                    .delete()
                    .addOnSuccessListener {
                        Toast.makeText(
                            view.context,
                            "${favMovie.movieTitle} deleted from  favorites",
                            Toast.LENGTH_SHORT
                        ).show()
                        dialog.cancel()
                    }
                    .addOnFailureListener {
                        Toast.makeText(
                            view.context,
                            "There was an error :(",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }
            builder.setNegativeButton(android.R.string.no) { dialog, which ->
                dialog.cancel()
            }
            builder.show()
        }
    }


    override fun parseJson(datos: Bundle) {
        val movieList = ArrayList<Movie>()
        val jsonData = JSONObject(datos.getString(JSON))
        val movie = Movie(
            false,
            jsonData.getString("backdrop_path"),
            listOf(1, 2, 3),
            jsonData.getInt("id"),
            jsonData.getString("original_language"),
            jsonData.getString("original_title"),
            jsonData.getString("overview"),
            jsonData.getDouble("popularity"),
            jsonData.getString("poster_path"),
            jsonData.getString("release_date"),
            jsonData.getString("title"),
            false,
            jsonData.getDouble("vote_average"),
            jsonData.getInt("vote_count")
        )
        val x = movie?.backdrop_path
        val y = movie?.poster_path
        if (x != null || y != null) {
            movie?.backdrop_path = "https://image.tmdb.org/t/p/w500/$y"
            movie.poster_path = "https://image.tmdb.org/t/p/original/$x"
            movieList.add(movie!!)
        }

        if (movie != null) {
            val view=viewToShow!!
            Toast.makeText(view.context, " ${movie.title}", Toast.LENGTH_SHORT).show()
            val intent = Intent(view.context, Activity_MovieDetails::class.java)
            intent.putExtra(MOVIE_TRANSFER, movie)
            intent.putExtra(USERNAME, logInModel.user.value)
            intent.putExtra(IS_LOGED, logInModel.flag.value)
            view.context.startActivity(intent)
        }

    }
}//End Of Class