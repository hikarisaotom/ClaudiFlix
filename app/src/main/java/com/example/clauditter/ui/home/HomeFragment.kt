package com.example.clauditter.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.clauditter.R
import com.example.clauditter.adapters.MoviePreviewAdapter
import com.example.clauditter.ui.clases.Movie
import com.google.gson.Gson


import kotlinx.android.synthetic.main.fragment_home.*
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
private const val  TAG="fragmentHome"
class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    /**RECYCLER ADAPTER */
    private val previewAdapter:MoviePreviewAdapter=MoviePreviewAdapter(ArrayList())

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val textView: TextView = root.findViewById(R.id.lbl_home)
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        /** DOWNLOADING DATA*/
        val client:OkHttpClient=OkHttpClient()
        downloadData(client,createUrl())
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView_CategoriasHome.layoutManager= LinearLayoutManager(activity)
        recyclerView_CategoriasHome.adapter=previewAdapter

    }


    fun createUrl():String{
        //"https://api.themoviedb.org/3/discover/movie?api_key=7a30c243665f85825e8f7e6ae19711fa&query=sort_by=popularity.desc"

        val url = HttpUrl.Builder()

            .scheme("https")
            .host("api.themoviedb.org")
            .addPathSegment("3")
            .addPathSegment("discover")
            .addPathSegment("movie")
            .addQueryParameter("api_key", getString(R.string.api_key))
            .addQueryParameter("sort_by", "popularity.desc")
            .build()
        return url.toString()
    }
    fun downloadData(client: OkHttpClient, url:String) {
        val request = Request.Builder()
            .url(url)
            .build()
        //With enqueue we run the call in a background thread
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {//it is called when a failure happends
                Log.d(TAG,"A failure has ocurred on the okHttp request")
                e.printStackTrace()
            }
            override fun onResponse(call: Call, response: Response) {//it is called when we get any response from te app
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")
                    var rawData:String=response.body!!.string()
                    activity?.runOnUiThread {
                        parseJson(rawData)
                    }
                }
            }
        })
    }
    fun parseJson(JSON:String) {
        val movieList = ArrayList<Movie>()
        val jsonData = JSONObject(JSON)
        val itemArray = jsonData.getJSONArray("results")
        for (i in 0 until itemArray.length()) {
            val jsonObject = itemArray.getJSONObject(i)
            val movie=convertJSon(jsonObject.toString())
            movieList.add(movie!!)
        }
        previewAdapter.loadNewData(movieList)
    }
    fun convertJSon(JSON:String): Movie?{
        val gson = Gson()
        val movie: Movie = gson.fromJson(JSON, Movie::class.java)
        return movie
    }
}