package com.example.clauditter.Listeners

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.clauditter.MainActivity
import com.example.clauditter.adapters.MoviePreviewAdapter
import com.example.clauditter.ui.clases.Movie
import com.example.clauditter.ui.home.JSON
import com.example.clauditter.ui.home.URL_DOWNLOAD
import com.google.gson.Gson
import org.json.JSONObject



class RecyclerItemsListeners
    (context:Context,
    recyclerView:RecyclerView,
    private val listener:OnRecyclerClickListener):RecyclerView.SimpleOnItemTouchListener() {
    private val TAG = "RecyclerItemClickListen"


    private val gestureDetector =
        GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapUp(e: MotionEvent): Boolean {
                //we optain the element which is being touch
                val childView = recyclerView.findChildViewUnder(e.x, e.y)!!
                //calling the action method
                listener.onItemClick(childView, recyclerView.getChildAdapterPosition(childView))
                return true
            }

            override fun onLongPress(e: MotionEvent) {
                val childView = recyclerView.findChildViewUnder(e.x, e.y)!!
                listener.onItemLongClick(childView, recyclerView.getChildAdapterPosition(childView))
            }
        })


    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        val result = gestureDetector.onTouchEvent(e)
        return result
    }
}


//FOR PAGINATION
class RecyclerScrollListener(val previewAdapter:MoviePreviewAdapter):RecyclerView.OnScrollListener(),OnDownloadComplete{
    var loading = true
    var pastVisiblesItems = 0
    var visibleItemCount:Int = 0
    var totalItemCount:Int = 0
    var page:Int = 2

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy);
        if (dx > 0) { //check for scroll down
            visibleItemCount = recyclerView?.layoutManager!!.childCount
            totalItemCount = recyclerView?.layoutManager!!.itemCount
            pastVisiblesItems = (recyclerView.layoutManager as LinearLayoutManager?)!!.findFirstVisibleItemPosition()
            if (loading) {
                if (visibleItemCount + pastVisiblesItems >= totalItemCount) {
                    loading = false
                    loading = true
                    val funciones=MainActivity()
                    val pathSegment=funciones.createUrl("movie/${previewAdapter.tag}",page.toString())
                    Toast.makeText(recyclerView.context,"Downloading page $page",Toast.LENGTH_SHORT).show()
                    var datos=Bundle()
                    datos.putString(URL_DOWNLOAD,pathSegment)
                    funciones.downloadData(datos,this)
                    page += 1
                }
            }
        }

    }

    override fun parseJson(datos:Bundle) {
        var listMoviesFull = ArrayList<Movie>()
        val movieList = ArrayList<Movie>()
        val jsonData = JSONObject(datos.getString(JSON))
        val itemArray = jsonData.getJSONArray("results")
        for (i in 0 until itemArray.length()) {
            val jsonObject = itemArray.getJSONObject(i)
            val movie = convertJSon(jsonObject.toString())
            val x = movie?.backdrop_path
            val y = movie?.poster_path
            if (x != null || y != null) {
                movie?.backdrop_path = "https://image.tmdb.org/t/p/w500/$y"
                movie.poster_path = "https://image.tmdb.org/t/p/original/$x"
                movieList.add(movie!!)
            }
        }
             previewAdapter.addNewMovies(movieList)

        }


    fun convertJSon(JSON: kotlin.String): Movie? {
        val gson = Gson()
        val movie: Movie = gson.fromJson(JSON, Movie::class.java)
        return movie
    }
}





