package com.example.clauditter.Listeners

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView

class RecyclerPreviewListener
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