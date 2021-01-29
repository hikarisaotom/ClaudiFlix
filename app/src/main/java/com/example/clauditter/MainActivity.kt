package com.example.clauditter

//Moshi

import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.clauditter.adapters.MoviePreviewAdapter
import com.example.clauditter.ui.clases.Movie

import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_home.*
import okhttp3.*
import okhttp3.HttpUrl
import okio.IOException
import org.json.JSONObject



private const val TAG="MainActivity"
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
 /*   /**RECYCLERVIEW ADAPTERS*/
    private val previewAdapter:MoviePreviewAdapter=MoviePreviewAdapter(ArrayList())
*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

       /* /** DOWNLOADING DATA*/
        val client:OkHttpClient=OkHttpClient()
        downloadData(client,createUrl())

        /** CONFIGURATION of RECYCLERVIEWS*/
        recyclerView_CategoriasHome.layoutManager= LinearLayoutManager(this)
        recyclerView_CategoriasHome.adapter=previewAdapter
*/

    }//fin onCreate

override fun onCreateOptionsMenu(menu: Menu): Boolean {
    // Inflate the menu; this adds items to the action bar if it is present.
    menuInflater.inflate(R.menu.main, menu)
    return true
}//fin onCreateOptionsMenu

override fun onSupportNavigateUp(): Boolean {
    val navController = findNavController(R.id.nav_host_fragment)
    return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
}//fin onSupportNavigateuP


/*fun createUrl():String{
   //"https://api.themoviedb.org/3/discover/movie?api_key=7a30c243665f85825e8f7e6ae19711fa&query=sort_by=popularity.desc"

    val url = HttpUrl.Builder()

        .scheme("https")
        .host("api.themoviedb.org")
        .addPathSegment("3")
        .addPathSegment("discover")
        .addPathSegment("movie")
        .addQueryParameter("api_key", getString(R.string.api_key))
        .addQueryParameter("sort_by", "popularity.desc")
        .build()
    return url.toString()
}
    fun downloadData(client:OkHttpClient,url:String) {
        val request = Request.Builder()
                .url(url)
                .build()
        //With enqueue we run the call in a background thread
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {//it is called when a failure happends
                Log.d(TAG,"A failure has ocurred on the okHttp request")
                e.printStackTrace()
            }
            override fun onResponse(call: Call, response: Response) {//it is called when we get any response from te app
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")
                        var rawData:String=response.body!!.string()
                         runOnUiThread {
                             parseJson(rawData)
                         }
                }
            }
        })
    }
    fun parseJson(JSON:String) {
        val movieList = ArrayList<Movie>()
        val jsonData = JSONObject(JSON)
        val itemArray = jsonData.getJSONArray("results")
        for (i in 0 until itemArray.length()) {
            val jsonObject = itemArray.getJSONObject(i)
            val movie=convertJSon(jsonObject.toString())
            movieList.add(movie!!)
        }
        previewAdapter.loadNewData(movieList)
    }
    fun convertJSon(JSON:String):Movie?{
        val gson = Gson()
        val movie: Movie = gson.fromJson(JSON, Movie::class.java)
     return movie
}*/

}