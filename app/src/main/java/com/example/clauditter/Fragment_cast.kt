package com.example.clauditter

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.clauditter.Listeners.OnDownloadComplete
import com.example.clauditter.adapters.CastAdapter
import com.example.clauditter.ui.clases.Person
import com.example.clauditter.ui.home.JSON
import com.example.clauditter.ui.home.URL_DOWNLOAD
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment__trailers.*
import kotlinx.android.synthetic.main.fragment_cast.*
import org.json.JSONObject


class Fragment_cast : Fragment(),
OnDownloadComplete{


    /**RECYCLER ADAPTER */
    private val castAdapter: CastAdapter = CastAdapter(ArrayList())

    private val dataModel: ViewModel_MovieDetails by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("FRAGMENT ","CREANDO FRAGMENTO")
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       val root=inflater.inflate(R.layout.fragment_cast, container, false)


        if(dataModel.cast.value!!.size<=0){
            /**Downloading data*/
            val funciones=MainActivity()
            val url=funciones.createUrl("movie/${dataModel.movie.value?.id.toString()}/credits",null)
            val datos=Bundle()
            datos.putString(URL_DOWNLOAD,url)
            funciones.downloadData(datos,this)
        }
        dataModel.castLoaded.observe(viewLifecycleOwner, Observer {
                if(it){
                    progressB_cast.visibility=View.GONE
                    if(dataModel.cast.value!!.isEmpty()){//the request was made, but there aren`t actors to show
                        recyclerView_Actores.visibility=View.GONE
                        val sad = 0x1F61E
                        val verySad = 0x1F622
                        val shame = 0x1F613
                        val emoji1 = String(Character.toChars(sad))
                        val emoji2 = String(Character.toChars(verySad))
                        val emoji3 = String(Character.toChars(shame))
                        lbl_castTitle.text= "\n \n \n \nWe have bad news!!! $emoji1 $emoji2 $emoji3 \n \n" +
                                "There are no information about the cast for this movie "
                    }else{
                        lbl_castTitle.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.cast, 0, 0, 0);
                        recyclerView_Actores.visibility=View.VISIBLE
                    }
                }else{
                    recyclerView_Actores.visibility=View.INVISIBLE
                }
        })

        dataModel.cast.observe(viewLifecycleOwner, Observer {
            castAdapter.loadNewData(it)
        })

        return root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView_Actores.layoutManager = LinearLayoutManager(activity)
        recyclerView_Actores.adapter = castAdapter
    }


    override fun parseJson(datos:Bundle) {
        val cast = ArrayList<Person>()
        val jsonData = JSONObject(datos.getString(JSON))
        val itemArray = jsonData.getJSONArray("cast")
        for (i in 0 until itemArray.length()) {
            val jsonObject = itemArray.getJSONObject(i)
            val actor = convertJSon(jsonObject.toString())
            if(actor?.known_for_department!="Acting"){
                break;
            }
            val x = actor?.profile_path
            if (x != null ) {
                actor?.profile_path = String.format(getString(R.string.url_preview_img), actor?.profile_path)
                cast.add(actor!!)
            }
        }
        //castAdapter.loadNewData(cast)
        dataModel.loadNewCast(cast)
    }

    fun convertJSon(JSON: String): Person? {
        val gson = Gson()
        val person: Person = gson.fromJson(JSON, Person::class.java)
        return person
    }

}