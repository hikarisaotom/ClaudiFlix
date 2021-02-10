package com.example.clauditter.ui.home

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.clauditter.*
import com.example.clauditter.Listeners.OnDownloadComplete
import com.example.clauditter.adapters.CategoriasHomeAdapter
import com.example.clauditter.ui.clases.Movie
import com.example.clauditter.ui.clases.MovieList
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_home.*

import org.json.JSONObject


private const val TAG = "fragmentHome"
private const val INDEX = "index"
const val JSON = "JSON"
const val URL_DOWNLOAD = "JSON"
 val listToDomwload = arrayListOf<String>( "Now Playing", "Popular", "Top Rated", "Upcoming")
val fields = arrayListOf<String>( "now_playing", "popular","top_rated","upcoming")

class HomeFragment : Fragment(),OnDownloadComplete{

    private val logInModel: ViewModel_LogIn by activityViewModels()
    private lateinit var previewAdapter: CategoriasHomeAdapter
    var listMoviesFull = ArrayList<MovieList>()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val textView: TextView = root.findViewById(R.id.lbl_home)
        previewAdapter=CategoriasHomeAdapter(ArrayList(),logInModel.user.value!!,logInModel.flag.value!!)

        logInModel.user.observe(viewLifecycleOwner, Observer {
            textView.text = "Welcome To ClaudiFlix "+it
        })

       if(logInModel.movieListIsLoaded()){//downloading new data
            var i = 0
            while (i < listToDomwload.size) {
                val funciones=MainActivity()
                val pathSegment=funciones.createUrl("movie/${fields[i].toString()}","1")
                Log.d("url_ ","$pathSegment")
                var datos=Bundle()
                datos.putString(URL_DOWNLOAD,pathSegment)
                datos.putInt(INDEX,i)
               funciones.downloadData(datos,this)
                i++
            }
        }

        logInModel.isFull.observe(viewLifecycleOwner, Observer {
                    if(it) {
                        progresB_home.visibility = View.GONE
                        recyclerView_CategoriasHome.visibility=View.VISIBLE
                        sendNotification()
                    }else{
                        recyclerView_CategoriasHome.visibility=View.INVISIBLE
                    }
           })

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logInModel.movieList.observe(viewLifecycleOwner, Observer {
            previewAdapter.loadNewData(it)
        })
        recyclerView_CategoriasHome.layoutManager = LinearLayoutManager(activity)
        recyclerView_CategoriasHome.adapter = previewAdapter

    }


    private fun sendNotification(){
        //creating notification channel, this is necesarry for android Oreo and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.description = CHANNEL_DESCRIPTION

            val manager =
                requireActivity().applicationContext.getSystemService(
                    NotificationManager::class.java
                )
            manager.createNotificationChannel(channel)

        }

        // randomly recommendation of a movie
        val posList = (0..(fields.size-1)).random()// getting a position of a list
        val subList = (logInModel.movieList.value)!!.get(posList).moviesToShow
        val posMovie =
            (0..(subList.size - 1)).random() //getting a position of a movie
        val movieToShow = subList.get(posMovie)
        NotificationHelper().displayNotification(
            movieToShow,
            logInModel.user.value!!,
            logInModel.flag.value!!,
            requireActivity().applicationContext
        )
    }

    override fun parseJson(datos:Bundle) {
        val movieList = ArrayList<Movie>()
        val jsonData = JSONObject(datos.getString(JSON))
        val itemArray = jsonData.getJSONArray("results")
        for (i in 0 until itemArray.length()) {
            val jsonObject = itemArray.getJSONObject(i)
            val movie = convertJSon(jsonObject.toString())
            val x = movie?.backdrop_path
            val y = movie?.poster_path
            if (x != null || y != null) {
                movie?.backdrop_path = String.format(getString(R.string.url_details_img), y)
                movie?.poster_path = String.format(getString(R.string.url_preview_img), x)
                movieList.add(movie!!)
            }
        }
        val pos=datos.getInt(INDEX)
        val newListCategory = MovieList(listToDomwload[pos], movieList,fields[pos].toString())
        listMoviesFull.add(newListCategory)
        if (listMoviesFull.size>=2) {
            // previewAdapter.loadNewData(listMoviesFull)
            logInModel.loadMovieList(listMoviesFull)
        }
    }

    fun convertJSon(JSON: String): Movie? {
        val gson = Gson()
        val movie: Movie = gson.fromJson(JSON, Movie::class.java)
        return movie
    }
}