package com.example.clauditter.adapters



import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.clauditter.R
import com.example.clauditter.ui.clases.Favorite
import com.example.clauditter.ui.clases.Person


class FavoriteHolder(view: View): RecyclerView.ViewHolder(view){
    val thumbnail: ImageView =view.findViewById(R.id.img_profileActor)
    val actorName: TextView =view.findViewById(R.id.lbl_actorName)
    val character: TextView =view.findViewById(R.id.lbl_character)
    val rol: TextView =view.findViewById(R.id.lbl_rolDescription)
}
class FavoritesAdapter(private var favorites:List<Favorite>):
    RecyclerView.Adapter<FavoriteHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.actor,parent,false)
        return FavoriteHolder(view)
    }

    override fun getItemCount(): Int {
        return if(favorites.isNotEmpty()) favorites.size else 1
    }

    fun loadNewData(newCast:List<Favorite>){
        favorites=newCast
        notifyDataSetChanged()
    }

    fun getMovie(position:Int):Favorite?{
        return if(favorites.isNotEmpty())favorites[position] else null
    }

    override fun onBindViewHolder(holder: FavoriteHolder, position: Int) {
        if(favorites.isEmpty()){
            //TODO IMPLEMENT THUMBNAIL
            holder.actorName.text="Information not avaliable"
            holder.rol.text=""
            holder.character.text="Information not avaliable"
        }else{
            val favorite=favorites[position]
            Glide.with(holder.itemView)  //2
                .load(favorite.photo) //3
                .into(holder.thumbnail) //8
            holder.actorName.text="Movie: "
            holder.rol.text=favorite.movieTitle
            holder.character.text=""

        }
    }

}
