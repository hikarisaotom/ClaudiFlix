package com.example.clauditter.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.clauditter.R
import com.example.clauditter.ui.clases.Person


class CastHolder(view: View): RecyclerView.ViewHolder(view){
    val thumbnail: ImageView =view.findViewById(R.id.img_profileActor)
    val actorName: TextView =view.findViewById(R.id.lbl_actorName)
    val character: TextView =view.findViewById(R.id.lbl_character)
    val rol: TextView =view.findViewById(R.id.lbl_rolDescription)
}
class CastAdapter(private var cast:List<Person>):
    RecyclerView.Adapter<CastHolder>() {
    private val TAG="MoviePreviewAdapter"


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.actores,parent,false)
        return CastHolder(view)
    }

    override fun getItemCount(): Int {
        return if(cast.isNotEmpty()) cast.size else 1
    }

    fun loadNewData(newCast:List<Person>){
        cast=newCast
        notifyDataSetChanged()
    }

    fun getMovie(position:Int):Person?{
        return if(cast.isNotEmpty())cast[position] else null
    }

    override fun onBindViewHolder(holder: CastHolder, position: Int) {
        if(cast.isEmpty()){
            //TODO IMPLEMENT THUMBNAIL
            /*Glide.with(holder.itemView)  //2
                .load(actor.profile_path) //3
                /*.centerCrop() //4
                .placeholder(R.drawable.ic_menu_camera) //5
                .error(R.drawable.ic_menu_gallery) //6*/
                .into(holder.thumbnail) //8*/
            holder.actorName.text="Information not avaliable"
            holder.rol.text=""
            holder.character.text="Information not avaliable"
        }else{
            val actor=cast[position]
            Glide.with(holder.itemView)  //2
                .load(actor.profile_path) //3
                .into(holder.thumbnail) //8
            holder.actorName.text=actor.name
            holder.rol.text="interpretates"
            holder.character.text=actor.character

        }
    }

}