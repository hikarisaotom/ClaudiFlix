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
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.clauditter.*
import com.example.clauditter.adapters.CategoriasHomeAdapter
import com.example.clauditter.ui.clases.Movie
import com.example.clauditter.ui.clases.MovieList
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_home.*
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import kotlin.random.Random

private const val TAG = "fragmentHome"

class HomeFragment : Fragment() {


    private val logInModel: ViewModel_LogIn by activityViewModels()

    /**RECYCLER ADAPTER */
    private lateinit var previewAdapter: CategoriasHomeAdapter

    /**Listas de parametros y nombres*/
    val listToDomwload = arrayListOf<String>( "Now Playing", "Popular", "Top Rated", "Upcoming")
    val fields = arrayListOf<String>( "now_playing", "popular","top_rated","upcoming")
    var listMoviesFull = ArrayList<MovieList>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val textView: TextView = root.findViewById(R.id.lbl_home)
        previewAdapter=CategoriasHomeAdapter(ArrayList(),logInModel.user.value!!,logInModel.flag.value!!)

        logInModel.user.observe(viewLifecycleOwner, Observer {
            textView.text = "Welcome To ClaudiFlix "+it
        })

       if(logInModel.movieListIsLoaded()){//downloading new data
           Toast.makeText(activity,"downloading new data",Toast.LENGTH_LONG).show()
            var i = 0
            while (i < listToDomwload.size) {
                val client: OkHttpClient = OkHttpClient()
                downloadData(client, createUrl(i), i)
                i++
            }


        }

        logInModel.movieList.observe(viewLifecycleOwner, Observer {
            previewAdapter.loadNewData(it)
            if(it.size>=4){
                //creating notification channel, this is necesarry for android Oreo and above
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
                    val channel=NotificationChannel(CHANNEL_ID, CHANNEL_NAME,NotificationManager.IMPORTANCE_DEFAULT)
                    channel.description= CHANNEL_DESCRIPTION
                    val manager=requireActivity().applicationContext.getSystemService(NotificationManager::class.java)
                    manager.createNotificationChannel(channel)
                }
                Toast.makeText(activity,"entra?",Toast.LENGTH_SHORT).show()
                // randomly recommendation ofa movie
                val posList = (0..(it.size-1)).random()// getting a position of a list
                val subList =(logInModel.movieList.value)!!.get(posList).moviesToShow
                val posMovie = (0..(subList.size-1)).random() //getting a position of a movie
                val movieToShow = subList.get(posMovie)
                NotificationHelper().displayNotification(movieToShow,
                    logInModel.user.value!!,
                    logInModel.flag.value!!,
                    requireActivity().applicationContext)
            }
           })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView_CategoriasHome.layoutManager = LinearLayoutManager(activity)
        recyclerView_CategoriasHome.adapter = previewAdapter
    }


    fun createUrl(index: Int): String {
        val url = HttpUrl.Builder()
            .scheme("https")
            .host("api.themoviedb.org")
            .addPathSegment("3")
            .addPathSegment("movie")
            .addPathSegment(fields[index])
            .addQueryParameter("api_key", getString(R.string.api_key))
            .build()

        return url.toString()
    }

    fun downloadData(client: OkHttpClient, url: String, index: Int) {

        val request = Request.Builder()
            .url(url)
            .build()
        //With enqueue we run the call in a background thread
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(
                call: Call,
                e: IOException
            ) {//it is called when a failure happends
                Log.d(TAG, "A failure has ocurred on the okHttp request")
                //  e.printStackTrace()
            }

            override fun onResponse(
                call: Call,
                response: Response
            ) {//it is called when we get any response from te app
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")
                    var rawData: String = response.body!!.string()
                    activity?.runOnUiThread {
                        parseJson(rawData, index)
                    }
                }
            }
        })
    }

    fun parseJson(JSON: String, index: Int) {
        val movieList = ArrayList<Movie>()
        val jsonData = JSONObject(JSON)
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
        val newListCategory = MovieList(listToDomwload[index], movieList)
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