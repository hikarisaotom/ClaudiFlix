package com.example.clauditter

//Moshi

import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.clauditter.Listeners.OnDownloadComplete
import com.example.clauditter.ui.home.JSON
import com.example.clauditter.ui.home.URL_DOWNLOAD
import com.google.android.material.navigation.NavigationView
import okhttp3.*
import java.io.IOException

private const val TAG = "MainActivity"

class MainActivity : BaseActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar_Movie)
        setSupportActionBar(toolbar)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_LogIn, R.id.nav_Favorites
            ), drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
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


    fun downloadData(datos: Bundle, listener: OnDownloadComplete) {
        val client: OkHttpClient = OkHttpClient()
        val request = Request.Builder()
            .url(datos.getString(URL_DOWNLOAD)!!)
            .build()
        //With enqueue we run the call in a background thread
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(
                call: Call,
                e: IOException
            ) {//it is called when a failure happends
                Log.d("error", "A failure has ocurred on the okHttp request")
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
                        datos.putString(JSON, rawData)
                        listener.parseJson(datos)
                    }
                }
            }
        })
    }

    fun createUrl(pathSegments: String, querys: String?): String {
        var url = HttpUrl.Builder()
            .scheme("https")
            .host("api.themoviedb.org")
            .addPathSegment("3")
            .addPathSegments(pathSegments)
            .addQueryParameter("api_key", "7a30c243665f85825e8f7e6ae19711fa")
        if (querys != null) {
            url = url.addQueryParameter("page", querys)
        }
        val resp = url.build()
        return url.toString()
    }
}