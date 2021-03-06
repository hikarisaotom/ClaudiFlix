package com.example.clauditter

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.clauditter.Listeners.OnDownloadComplete
import com.example.clauditter.adapters.ReviewAdapter
import com.example.clauditter.ui.clases.AuthorDetails
import com.example.clauditter.ui.clases.Review
import com.example.clauditter.ui.home.JSON
import com.example.clauditter.ui.home.URL_DOWNLOAD
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_reviews.*
import org.json.JSONObject


class Fragment_reviews : Fragment(),
OnDownloadComplete{
    private val dataModel: ViewModel_MovieDetails by activityViewModels()
    /**RECYCLER ADAPTER */
    private val reviewAdapter: ReviewAdapter = ReviewAdapter(ArrayList())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /**Downloading data*/
        val funciones=MainActivity()

        val url=funciones.createUrl("movie/${dataModel.movie.value?.id.toString()}/reviews",null)

        val datos=Bundle()
        datos.putString(URL_DOWNLOAD,url)
        funciones.downloadData(datos,this)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root=inflater.inflate(R.layout.fragment_reviews, container, false)
        if(dataModel.reviews.value!!.size<=0){
            /**Downloading data*/
            val funciones=MainActivity()
            val url=funciones.createUrl("movie/${dataModel.movie.value?.id.toString()}/reviews",null)
            val datos=Bundle()
            datos.putString(URL_DOWNLOAD,url)
            funciones.downloadData(datos,this)
        }

        dataModel.reviews.observe(viewLifecycleOwner, Observer {
            reviewAdapter.loadNewData(it)
        })

        dataModel.reviewsLoaded.observe(viewLifecycleOwner, Observer {
            if(it){
                progressB_reviews.visibility=View.GONE
                if(dataModel.reviews.value!!.isEmpty()){//the request was made, but there aren`t actors to show
                    recycler_Reviews.visibility=View.GONE
                    val sad = 0x1F61E
                    val verySad = 0x1F622
                    val shame = 0x1F613
                    val emoji1 = String(Character.toChars(sad))
                    val emoji2 = String(Character.toChars(verySad))
                    val emoji3 = String(Character.toChars(shame))
                    lbl_reviewsTitle.text= "\n \n \n \nWe have bad news!!! $emoji1 $emoji2 $emoji3 \n \n" +
                            "There are no reviews available for this movie yet"
                }else{
                    lbl_reviewsTitle.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.review, 0, 0, 0);
                    recycler_Reviews.visibility=View.VISIBLE
                }
            }else{
                recycler_Reviews.visibility=View.INVISIBLE
            }
        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler_Reviews.layoutManager = LinearLayoutManager(activity)
        recycler_Reviews.adapter = reviewAdapter

    }


    override fun parseJson(datos:Bundle) {
        val reviews = ArrayList<Review>()
        val jsonData = JSONObject(datos.getString(JSON))
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
      //  reviewAdapter.loadNewData(reviews)
        dataModel.loadNewReviews(reviews)
    }

    fun convertJSon(JSON: String): AuthorDetails? {
        val gson = Gson()
        val person: AuthorDetails = gson.fromJson(JSON, AuthorDetails::class.java)
        return person
    }




}