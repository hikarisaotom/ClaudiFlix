package com.example.clauditter.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.clauditter.R
import com.example.clauditter.ui.clases.MovieList


class MovieViewHolder(view: View): RecyclerView.ViewHolder(view){
    val titleList:TextView=view.findViewById(R.id.lbl_tituloLista)
    val recyclerMovies:RecyclerView=view.findViewById(R.id.recyclerListaHome)
}

class CategoriasHomeAdapter(private var ListOfLists:List<MovieList>):RecyclerView.Adapter<MovieViewHolder>() {
    private val TAG="CategoriasHomeAdap"
//this is called by the layout when a new view is needed
   override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.lista_home_recyclerview,parent,false)
        return MovieViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if(ListOfLists.isNotEmpty()) ListOfLists.size else 1
    }
    fun loadNewData(newLists:List<MovieList>){
        ListOfLists=newLists
        notifyDataSetChanged()
    }

    fun getList(position:Int):MovieList?{
        return if(ListOfLists.isNotEmpty())ListOfLists[position] else null
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        if(ListOfLists.isEmpty()){

        }else{

            val list=ListOfLists[position]
            holder.titleList.text=list.title

            /**RECYCLER ADAPTER */
            val previewAdapter:MoviePreviewAdapter=MoviePreviewAdapter(ArrayList())

           val  horizontalLayout = LinearLayoutManager(
                null,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            holder.recyclerMovies.layoutManager= horizontalLayout

            holder.recyclerMovies.adapter=previewAdapter
            previewAdapter.loadNewData(list.moviesToShow)
        }

    }
}