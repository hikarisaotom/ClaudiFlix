package com.example.clauditter

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.clauditter.adapters.CastAdapter
import com.example.clauditter.ui.clases.Person
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_cast.*
import okhttp3.*
import org.json.JSONObject
import java.io.IOException


class Fragment_cast : Fragment() {
    /**RECYCLER ADAPTER */
    private val castAdapter: CastAdapter = CastAdapter(ArrayList())

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("FRAGMENT ","CREANDO FRAGMENTO")
        super.onCreate(savedInstanceState)
        /**Downloading data*/
        val client: OkHttpClient = OkHttpClient()
        downloadData(client,createUrl("550"))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       val root=inflater.inflate(R.layout.fragment_cast, container, false)
        /**Downloading data*/
        val client: OkHttpClient = OkHttpClient()
        downloadData(client,createUrl("550"))
        return root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /**Downloading data*/
        val client: OkHttpClient = OkHttpClient()
        downloadData(client,createUrl("550"))
        recyclerView_Actores.layoutManager = LinearLayoutManager(activity)
        recyclerView_Actores.adapter = castAdapter
    }


    fun createUrl(movieId:String): String {
        val url = HttpUrl.Builder()
            .scheme("https")
            .host("api.themoviedb.org")
            .addPathSegment("3")
            .addPathSegment("movie")
            .addPathSegment(movieId.toString())
            .addPathSegment("credits")
            .addQueryParameter("api_key", getString(R.string.api_key))
            .build()
        return url.toString()
    }

    fun downloadData(client: OkHttpClient, url: String) {

        val request = Request.Builder()
            .url(url)
            .build()
        //With enqueue we run the call in a background thread
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(
                call: Call,
                e: IOException
            ) {//it is called when a failure happends
                Log.d("TAG", "A failure has ocurred on the okHttp request")
                //  e.printStackTrace()
            }

            override fun onResponse(
                call: Call,
                response: Response
            ) {//it is called when we get any response from te app
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")
                    var rawData: String = response.body!!.string()
                    activity?.runOnUiThread {
                        parseJson(rawData)
                    }
                }
            }
        })
    }

    fun parseJson(JSON: String) {
        val cast = ArrayList<Person>()
        val jsonData = JSONObject(JSON)
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
        castAdapter.loadNewData(cast)
    }

    fun convertJSon(JSON: String): Person? {
        val gson = Gson()
        val person: Person = gson.fromJson(JSON, Person::class.java)
        return person
    }

}