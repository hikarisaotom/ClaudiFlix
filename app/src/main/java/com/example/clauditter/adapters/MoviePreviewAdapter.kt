package com.example.clauditter.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.clauditter.ui.clases.Movie

import com.example.clauditter.R

//La creamos afuera por que si la creamos como clase inner (osea interna( puede dar errores de memoria
class MoviePreviewHolder(view: View): RecyclerView.ViewHolder(view){
    val thumbnail: ImageView =view.findViewById(R.id.img_preview)
    val title: TextView =view.findViewById(R.id.lblnamePreview)

}
class MoviePreviewAdapter(private var movies:List<Movie>): RecyclerView.Adapter<MoviePreviewHolder>() {
    private val TAG="MoviePreviewAdapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviePreviewHolder {
        Log.d(TAG,"onCreateViewhOLDER NEW View necesitada")
        val view= LayoutInflater.from(parent.context).inflate(R.layout.preview_movie,parent,false)
        return MoviePreviewHolder(view)
    }

    override fun getItemCount(): Int {
        return if(movies.isNotEmpty()) movies.size else 1
    }

    fun loadNewData(newMovies:List<Movie>){
        movies=newMovies
        notifyDataSetChanged()
    }

    fun getMovie(position:Int):Movie?{
        return if(movies.isNotEmpty())movies[position] else null
    }

    /*se llama por el recyclerview cuando se quiere alamcenar nueva data en el viewholder */
    override fun onBindViewHolder(holder: MoviePreviewHolder, position: Int) {
        if(movies.isEmpty()){
            holder.title.text="NO LIST AVALIABLE AT THE MOMENT"
        }else{

            val movieItem=movies[position]
            holder.title.text=movieItem.title


        }

    }
}