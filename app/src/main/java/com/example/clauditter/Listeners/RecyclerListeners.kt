package com.example.clauditter.Listeners

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.AbsListView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.clauditter.MainActivity
import com.example.clauditter.ui.home.HomeFragment
import com.example.clauditter.ui.home.JSON
import com.example.clauditter.ui.home.URL_DOWNLOAD
import java.lang.String
import java.net.URL


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
class RecyclerScrollListener:RecyclerView.OnScrollListener(),OnDownloadComplete{
    var loading = true
    var pastVisiblesItems = 0
    var visibleItemCount:Int = 0
    var totalItemCount:Int = 0
    var page:Int = 0

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy);
        if (dx > 0) { //check for scroll down
            visibleItemCount = recyclerView?.layoutManager!!.childCount
            totalItemCount = recyclerView?.layoutManager!!.itemCount
            pastVisiblesItems = (recyclerView.layoutManager as LinearLayoutManager?)!!.findFirstVisibleItemPosition()
            if (loading) {
                if (visibleItemCount + pastVisiblesItems >= totalItemCount) {
                    loading = false
                    page=page++
                    loading = true
                    val url="https://api.themoviedb.org/3/movie/550?api_key=7a30c243665f85825e8f7e6ae19711fa"
                    var bundle=Bundle()
                    bundle.putString(URL_DOWNLOAD,url)
                    MainActivity().downloadData(bundle,this)
                }
            }
        }

    }

    override fun parseJson(data:Bundle) {
        Log.d("FUNCIONA","${data.getString(JSON)}")
    }
}




