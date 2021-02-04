package com.example.clauditter.ui.Login

import android.R.attr
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.clauditter.LogViewModel
import com.example.clauditter.R
import kotlinx.android.synthetic.main.fragment_login.*
import okhttp3.*
import org.json.JSONObject
import java.io.IOException


class LogInFragment : Fragment() {
    private lateinit var galleryViewModel: LogInViewModel
    private val logInModel: LogViewModel by activityViewModels()
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_login, container, false)
        val textView: TextView = root.findViewById(R.id.text_gallery)

        logInModel.flag.observe(viewLifecycleOwner, Observer {
            if(it){
                btn_logOut.visibility=View.VISIBLE
                lbl_goodbye.visibility=View.VISIBLE
                text_gallery.visibility=View.GONE
                txtUsername_logIn.visibility=View.GONE
                txtPassword_logIn.visibility=View.GONE
                lblPassword.visibility=View.GONE
                lblUsername.visibility=View.GONE
                btnLogIn.visibility=View.GONE
            }else{
                btn_logOut.visibility=View.GONE
                lbl_goodbye.visibility=View.GONE
                text_gallery.visibility=View.VISIBLE
                txtUsername_logIn.visibility=View.VISIBLE
                txtPassword_logIn.visibility=View.VISIBLE
                lblPassword.visibility=View.VISIBLE
                lblUsername.visibility=View.VISIBLE
                btnLogIn.visibility=View.VISIBLE
            }
        })
        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Setting to the btn login the action to call validateAccess method
        btnLogIn.setOnClickListener(View.OnClickListener {
            validateAccess()
        })
        btn_logOut.setOnClickListener(View.OnClickListener {
            logInModel.setFlag(false)
            logInModel.setUser("")
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
                            logInModel.setFlag(false)
                            logInModel.setUser("")
                        }
                        throw IOException()
                    }
                    activity?.runOnUiThread {
                        Toast.makeText(activity,"Welcome ${txtUsername_logIn.text.toString()}",Toast.LENGTH_LONG).show()
                        logInModel.setFlag(true)
                        logInModel.setUser(txtUsername_logIn.text.toString())
                    }


                }
            }
        })
    }


}