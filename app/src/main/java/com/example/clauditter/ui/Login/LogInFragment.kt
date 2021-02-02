package com.example.clauditter.ui.Login

import android.R.attr
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.clauditter.R
import kotlinx.android.synthetic.main.fragment_login.*
import okhttp3.*
import org.json.JSONObject
import java.io.IOException


class LogInFragment : Fragment() {

    private lateinit var galleryViewModel: LogInViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        galleryViewModel =
                ViewModelProvider(this).get(LogInViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_login, container, false)
        val textView: TextView = root.findViewById(R.id.text_gallery)
        galleryViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Setting to the btn login the action to call validateAccess method
        btnLogIn.setOnClickListener(View.OnClickListener {
            validateAccess()
        })

    }//fin de metodo



    fun createUrl(field:String): String {
        val url = HttpUrl.Builder()
            .scheme("https")
            .host("api.themoviedb.org")
            .addPathSegment("3")
            .addPathSegment("authentication")
            .addPathSegment("token")
            .addPathSegment(field)
            .addQueryParameter("api_key", getString(R.string.api_key))
            .build()
        return url.toString()
    }
    fun validateAccess(){
        val url= createUrl("new")
        val client: OkHttpClient = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException
            ) {}
            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")
                    var rawData: String = response.body!!.string()
                    activity?.runOnUiThread {
                        val jsonData = JSONObject(rawData)
                        makePost(OkHttpClient(),createUrl("validate_with_login"),jsonData.getString("request_token").toString())
                    }
                }
            }
        })
    }
    fun makePost(client: OkHttpClient, url: String,token:String) {
        val requestBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("username", txtUsername_logIn.text.toString())
            .addFormDataPart("password", txtPassword_logIn.text.toString())
            .addFormDataPart("request_token",token )
            .build()
        val request=Request.Builder()
            .url(url)
            .post(requestBody)
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {

                Toast.makeText(activity,"Please check your data",Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) {
                        activity?.runOnUiThread {
                            //todo agregar snackbar
                            Toast.makeText(activity,"Please check your data",Toast.LENGTH_LONG).show()
                        }
                        throw IOException()
                    }

                    activity?.runOnUiThread {
                        Toast.makeText(activity,"Welcome ${txtUsername_logIn.text.toString()}",Toast.LENGTH_LONG).show()

                    }
                }
            }
        })
    }


}