package com.example.clauditter

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.clauditter.Listeners.OnDownloadComplete
import com.example.clauditter.Listeners.OnRecyclerClickListener
import com.example.clauditter.Listeners.RecyclerItemsListeners
import com.example.clauditter.adapters.TRAILER_KEY
import com.example.clauditter.adapters.TrailerAdapter
import com.example.clauditter.ui.clases.Movie
import com.example.clauditter.ui.clases.Trailer
import com.example.clauditter.ui.home.JSON
import com.example.clauditter.ui.home.URL_DOWNLOAD
import com.google.gson.Gson
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import kotlinx.android.synthetic.main.fragment__trailers.*
import kotlinx.android.synthetic.main.movie_description.*

class Fragment_Trailers : Fragment(),
    OnRecyclerClickListener,
OnDownloadComplete{
    private val dataModel: ViewModel_MovieDetails by activityViewModels()
    /**RECYCLER ADAPTER */
    private val trailerAdapter: TrailerAdapter = TrailerAdapter(ArrayList())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root= inflater.inflate(R.layout.fragment__trailers, container, false)

        if(dataModel.trailers.value!!.size<=0){
            /**Downloading data*/
            val funciones=MainActivity()

            val url=funciones.createUrl("movie/${dataModel.movie.value?.id.toString()}/videos",null)
            val datos=Bundle()
            datos.putString(URL_DOWNLOAD,url)
            funciones.downloadData(datos,this)
        }

        dataModel.trailers.observe(viewLifecycleOwner, Observer {
            trailerAdapter.loadNewData(it)
        })

        return root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lbl_trailers.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.cast, 0, 0, 0)
        recycler_Trailers.layoutManager = LinearLayoutManager(activity)
        recycler_Trailers.adapter = trailerAdapter
        recycler_Trailers.addOnItemTouchListener(RecyclerItemsListeners(view.context,recycler_Trailers,this))

    }





   override fun parseJson(datos:Bundle) {
        val trailers = ArrayList<Trailer>()
        val jsonData = JSONObject(datos.getString(JSON))
        val itemArray = jsonData.getJSONArray("results")
        for (i in 0 until itemArray.length()) {
            val jsonObject = itemArray.getJSONObject(i)
            val trailer = convertJSon(jsonObject.toString())
                trailers.add(trailer!!)
        }
       // trailerAdapter.loadNewData(trailers)
        dataModel.loadNewTrailers(trailers)
    }

    fun convertJSon(JSON: String): Trailer? {
        val gson = Gson()
        val person: Trailer = gson.fromJson(JSON, Trailer::class.java)
        return person
    }


    /**
     * Methods: an item was cliked
     */
    override fun onItemClick(view: View, position: Int) {
        val trailer = trailerAdapter.getTrailer(position)
        if (trailer != null) {
            if (trailer.name != null&&trailer.id!=null) {
                Toast.makeText(view.context, " ${trailer.name}", Toast.LENGTH_SHORT).show()
                val intent = Intent(view.context, Activity_YoutubeTrailers::class.java)
                intent.putExtra(TRAILER_KEY, trailer.key)
                view.context.startActivity(intent)
            }else{
                Toast.makeText(view.context, " Action not supported :( ", Toast.LENGTH_SHORT).show()

            }
        }
    }

    override fun onItemLongClick(view: View, position: Int) {

    }



}