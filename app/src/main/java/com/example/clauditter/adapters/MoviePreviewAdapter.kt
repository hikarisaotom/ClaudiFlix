package com.example.clauditter.adapters


import android.content.res.Resources
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.clauditter.R
import com.example.clauditter.ui.clases.Movie


//Glide


class MoviePreviewHolder(view: View): RecyclerView.ViewHolder(view){
    val thumbnail: ImageView =view.findViewById(R.id.img_preview)


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

        }else{
            val movieItem=movies[position]
            Glide.with(holder.itemView)  //2
                .load(movieItem.backdrop_path) //3
                /*.centerCrop() //4
                .placeholder(R.drawable.ic_menu_camera) //5
                .error(R.drawable.ic_menu_gallery) //6*/
                .into(holder.thumbnail) //8
        }

    }
}