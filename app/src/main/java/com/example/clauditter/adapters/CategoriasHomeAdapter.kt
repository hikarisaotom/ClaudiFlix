package com.example.clauditter.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.clauditter.R
import com.example.clauditter.ui.clases.MovieList


class MovieViewHolder(view: View): RecyclerView.ViewHolder(view){
    val recyclerlista:RecyclerView=view.findViewById(R.id.recyclerListaHome)
}
/*
class CategoriasHomeAdapter(private var ListOfLists:List<MovieList>):RecyclerView.Adapter<FlickrImageViewHolder>() {
    private val TAG="CategoriasHomeAdap"
//this is called by the layout when a new view is needed
   override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {

        val view= LayoutInflater.from(parent.context).inflate(R.layout.descripcion_peliculas,parent,false)
        return MovieViewHolder(view)
    }

    override fun getItemCount(): Int {
        //Log.d(TAG,"getItemCount")
        return if(ListOfLists.isNotEmpty()) ListOfLists.size else 1
    }
    /*toma la nueva lista de fotos y hace que la lista que se va a mostrar (photolist) sea igual a ella.
    * luego le notifica a la vista que los datas cambiaron y que entonces deben actuaizarse */
    fun loadNewData(newLists:List<MovieList>){
        ListOfLists=newLists
        //esto actuaiza la vista
        notifyDataSetChanged()
    }

    fun getList(position:Int):MovieList?{
        return if(ListOfLists.isNotEmpty())ListOfLists[position] else null
    }

    /*se llama por el recyclerview cuando se quiere alamcenar nueva data en el viewholder */
    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        /*if(ListOfLists.isEmpty()){

        }else{
            //llamado por el layout manager cuando quiere nueva data en una vista existente.
            //1)obtenemos el objeto a cargar
            val list=ListOfLists[position]


        }*/

    }
}*/