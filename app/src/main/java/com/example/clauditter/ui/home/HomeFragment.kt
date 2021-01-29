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
import com.example.clauditter.R
import com.example.clauditter.adapters.CategoriasHomeAdapter
import com.example.clauditter.ui.clases.Movie
import com.example.clauditter.ui.clases.MovieList
import com.google.gson.Gson


import kotlinx.android.synthetic.main.fragment_home.*
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
private const val  TAG="fragmentHome"
class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    /**RECYCLER ADAPTER */
    private val previewAdapter:CategoriasHomeAdapter=CategoriasHomeAdapter(ArrayList())
    /**Listas de parametros y nombres*/
    val listToDomwload = arrayListOf<String>("Top Rated","Now showing","Another")
    val fields = arrayListOf<String>("","primary_release_date.gte","primary_release_year")
    val queryParams = arrayListOf<String>("","2014-09-15","2010")
    val sortOrders = arrayListOf<String>("popularity.desc","popularity.desc","vote_average.desc")
    var listMoviesFull=ArrayList<MovieList>()
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
        var i=0
        while (i<3){
            val client:OkHttpClient=OkHttpClient()
            downloadData(client,createUrl(i),i)
            i++
        }


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG,"_________________TOTAL CATEGORIES_______________")
        Log.d(TAG,"${listMoviesFull.size}")
        recyclerView_CategoriasHome.layoutManager= LinearLayoutManager(activity)
        recyclerView_CategoriasHome.adapter=previewAdapter
    }


    fun createUrl(index:Int):String{
        val url = HttpUrl.Builder()
            .scheme("https")
            .host("api.themoviedb.org")
            .addPathSegment("3")
            .addPathSegment("discover")
            .addPathSegment("movie")
            .addQueryParameter("api_key", getString(R.string.api_key))
            .addQueryParameter(fields[index], queryParams[index])
            .addQueryParameter("sort_by", sortOrders[index])
            .build()

        return url.toString()
    }
    fun downloadData(client: OkHttpClient, url:String,index:Int) {
        Log.d(TAG,"_________________URL # ${index}_______________")
        Log.d(TAG,"$url")
        val request = Request.Builder()
            .url(url)
            .build()
        //With enqueue we run the call in a background thread
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {//it is called when a failure happends
                Log.d(TAG,"A failure has ocurred on the okHttp request")
                //  e.printStackTrace()
            }
            override fun onResponse(call: Call, response: Response) {//it is called when we get any response from te app
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")
                    var rawData:String=response.body!!.string()
                    activity?.runOnUiThread {
                        parseJson(rawData,index)
                    }
                }
            }
        })
    }
    fun parseJson(JSON:String,index: Int) {
        val movieList = ArrayList<Movie>()
        val jsonData = JSONObject(JSON)
        val itemArray = jsonData.getJSONArray("results")
        for (i in 0 until itemArray.length()) {
            val jsonObject = itemArray.getJSONObject(i)
            val movie=convertJSon(jsonObject.toString())
            movie?.backdrop_path=String.format(getString(R.string.url_details_img),movie?.poster_path)
            movie?.poster_path=String.format(getString(R.string.url_preview_img),movie?.backdrop_path)
            movieList.add(movie!!)
        }
        val newListCategory=MovieList(listToDomwload[index],movieList)
        listMoviesFull.add(newListCategory)
        if(index==2){
            previewAdapter.loadNewData(listMoviesFull)
        }
    }
    fun convertJSon(JSON:String): Movie?{
        val gson = Gson()
        val movie: Movie = gson.fromJson(JSON, Movie::class.java)
        return movie
    }
}