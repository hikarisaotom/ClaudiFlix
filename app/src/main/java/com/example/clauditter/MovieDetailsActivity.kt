package com.example.clauditter

import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.clauditter.adapters.CategoriasHomeAdapter
import com.example.clauditter.ui.clases.Movie
import com.example.clauditter.adapters.CastAdapter
import com.example.clauditter.ui.clases.Person
import com.google.gson.Gson
import kotlinx.android.synthetic.main.movie_description.*
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

private const val TAG="DETAILS"
class MovieDetailsActivity : AppCompatActivity() {
private var movieId:Int=0
    /**RECYCLER ADAPTER */
    private val castAdapter: CastAdapter = CastAdapter(ArrayList())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.movie_description)
        setSupportActionBar(findViewById(R.id.toolbar))
        //Movie transfer is declared into MoviePreviewAdapter
           val movie=intent.getParcelableExtra<Movie>(MOVIE_TRANSFER) as Movie
        //Corregir el link de la imagen
        Glide.with(this)
            .load(movie.poster_path)
            .into(img_vistaPelicula)
            movieId=movie.id

        /**Downloading data*/
        val client: OkHttpClient = OkHttpClient()
        downloadData(client,createUrl())

    }

    fun createUrl(): String {
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
        Log.d(TAG,"_________URL DE ACTORES_______")
        Log.d(TAG,"${url}")
        val request = Request.Builder()
            .url(url)
            .build()
        //With enqueue we run the call in a background thread
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(
                call: Call,
                e: IOException
            ) {//it is called when a failure happends
                Log.d(TAG, "A failure has ocurred on the okHttp request")
                //  e.printStackTrace()
            }

            override fun onResponse(
                call: Call,
                response: Response
            ) {//it is called when we get any response from te app
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")
                    var rawData: String = response.body!!.string()
                   runOnUiThread {
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
        Log.d(TAG,"CANTIDAD DE ACTORES________- ${cast.size}")
            castAdapter.loadNewData(cast)
    }

    fun convertJSon(JSON: String): Person? {
        val gson = Gson()
        val person: Person = gson.fromJson(JSON, Person::class.java)
        return person
    }
}//end of class