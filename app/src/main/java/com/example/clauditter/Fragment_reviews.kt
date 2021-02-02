package com.example.clauditter

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.clauditter.adapters.CastAdapter
import com.example.clauditter.adapters.ReviewAdapter
import com.example.clauditter.ui.clases.AuthorDetails
import com.example.clauditter.ui.clases.Movie
import com.example.clauditter.ui.clases.Review
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_cast.*
import kotlinx.android.synthetic.main.fragment_reviews.*
import okhttp3.*
import org.json.JSONObject
import java.io.IOException


class Fragment_reviews : Fragment() {
    /**RECYCLER ADAPTER */
    private val reviewAdapter: ReviewAdapter = ReviewAdapter(ArrayList())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var movieToShow = requireArguments().getParcelable<Movie>(MOVIE_TRANSFER)
        /**Downloading data*/
        val client: OkHttpClient = OkHttpClient()
        downloadData(client,createUrl(movieToShow?.id.toString()))

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root=inflater.inflate(R.layout.fragment_reviews, container, false)
        var movieToShow = requireArguments().getParcelable<Movie>(MOVIE_TRANSFER)

        /**Downloading data*/
        val client: OkHttpClient = OkHttpClient()
        downloadData(client,createUrl(movieToShow?.id.toString()))
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler_Reviews.layoutManager = LinearLayoutManager(activity)
        recycler_Reviews.adapter = reviewAdapter
        lbl_reviewsTitle.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.review, 0, 0, 0);
    }
    fun createUrl(movieId:String): String {
        val url = HttpUrl.Builder()
            .scheme("https")
            .host("api.themoviedb.org")
            .addPathSegment("3")
            .addPathSegment("movie")
            .addPathSegment(movieId.toString())
            .addPathSegment("reviews")
            .addQueryParameter("api_key", getString(R.string.api_key))
            .build()
        return url.toString()
    }

    fun downloadData(client: OkHttpClient, url: String) {
        Log.d("URL ",url)
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
        val reviews = ArrayList<Review>()
        val jsonData = JSONObject(JSON)
        val itemArray = jsonData.getJSONArray("results")
        for (i in 0 until itemArray.length()) {
            val jsonObject = itemArray.getJSONObject(i)
            val details=jsonObject.getJSONObject("author_details")
            val content=jsonObject.getString("content")
            val authorDetail = convertJSon(details.toString())
            if(authorDetail?.avatar_path!=null){
                authorDetail?.avatar_path= if(authorDetail?.avatar_path.contains("https://secure.gravatar.com")){
                    val size=authorDetail?.avatar_path!!.length
                   authorDetail?.avatar_path.substring(1,size)
                }else{
                    "https://secure.gravatar.com/avatar"+authorDetail?.avatar_path
                }
            }
            reviews.add(Review(authorDetail!!,content))
        }
        Log.d("REVIEW","SE LLENO EL ARREGLO")
        reviewAdapter.loadNewData(reviews)
    }

    fun convertJSon(JSON: String): AuthorDetails? {
        val gson = Gson()
        val person: AuthorDetails = gson.fromJson(JSON, AuthorDetails::class.java)
        return person
    }




}