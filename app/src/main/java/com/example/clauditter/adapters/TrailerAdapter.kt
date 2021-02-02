package com.example.clauditter.adapters


import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.clauditter.Listeners.OnRecyclerClickListener
import com.example.clauditter.R
import com.example.clauditter.YoutubeTrailersActivity
import com.example.clauditter.ui.clases.Trailer

const val TRAILER_KEY = "TRAILER_KEY"

class TrailerHolder(view: View) : RecyclerView.ViewHolder(view) {
    val boton: Button = view.findViewById(R.id.btn_trailers)
}

class TrailerAdapter(private var trailers: List<Trailer>) :
    RecyclerView.Adapter<TrailerHolder>() {
    private val TAG = "YoutubeTrailerAdapter"


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrailerHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.trailer, parent, false)
        return TrailerHolder(view)
    }

    override fun getItemCount(): Int {
        return if (trailers.isNotEmpty()) trailers.size else 1
    }

    fun loadNewData(newTrailers: List<Trailer>) {
        trailers = newTrailers
        notifyDataSetChanged()
    }

    fun getTrailer(position: Int): Trailer? {
        return if (trailers.isNotEmpty()) trailers[position] else null
    }

    override fun onBindViewHolder(holder: TrailerHolder, position: Int) {
        if (trailers.isEmpty()) {
            holder.boton.text = "No Trailers Found"
        } else {
            val trailer = trailers[position]
            holder.boton.text = trailer.name

        }
    }




}