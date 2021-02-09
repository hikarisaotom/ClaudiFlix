package com.example.clauditter.ui.Login

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.clauditter.ViewModel_LogIn
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.SnackbarLayout
import kotlinx.android.synthetic.main.fragment_login.*
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import com.example.clauditter.R

class LogInFragment : Fragment() {

    private val logInModel: ViewModel_LogIn by activityViewModels()
    private var btnView: View? = null
    private var request_token: String = ""
    private var session_id: String = ""
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_login, container, false)
        logInModel.flag.observe(viewLifecycleOwner, Observer {
            var flagLogin = 0;
            var flagLogout = 0
            if (it) {
                flagLogout = View.VISIBLE
                flagLogin = View.GONE
            } else {
                flagLogout = View.GONE
                flagLogin = View.VISIBLE
            }
            progressB_login.visibility = View.GONE
            btn_logOut.visibility = flagLogout
            lbl_goodbye.visibility = flagLogout
            text_gallery.visibility = flagLogin
            txtUsername_logIn.visibility = flagLogin
            txtPassword_logIn.visibility = flagLogin
            lblPassword.visibility = flagLogin
            lblUsername.visibility = flagLogin
            btnLogIn.visibility = flagLogin
        })

        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnLogIn.setOnClickListener(View.OnClickListener {
            hideKeyBoard()
            validateAccess()
            progressB_login.visibility = View.VISIBLE
            btnView = it
        })
        btn_logOut.setOnClickListener(View.OnClickListener { closeSession() })
    }//fin de metodo


    override fun onPause() {
        hideKeyBoard()
        super.onPause()
    }


    fun hideKeyBoard() {
        val imm =
            activity?.applicationContext?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    /**LogIn and LogOut Methods*/
    private fun closeSession() {
        successLogOut()
        // https://api.themoviedb.org/3/authentication/session?api_key=<<api_key>>
        //https://www.themoviedb.org/logout
        /** No se por que, pero al tratar de generar un sessionID se produce un error y no lo puedo recuperar
        val url = HttpUrl.Builder()
        .scheme("https")
        .host("api.themoviedb.org")
        .addPathSegment("3")
        .addPathSegment("authentication")
        .addPathSegment("session")
        .addQueryParameter("api_key", getString(R.string.api_key))
        .build()
        val requestBody: RequestBody = MultipartBody.Builder()
        .setType(MultipartBody.FORM)
        .addFormDataPart("session_id", session_id)
        .build()
        makePost(OkHttpClient(), url.toString(), requestBody, false)*/

    }

    private fun getSessionId(request_token: String): String {
        // example https://api.themoviedb.org/3/authentication/session/new?api_key=<<api_key>>
        //example https://api.themoviedb.org/3/authentication/session/new?api_key=7a30c243665f85825e8f7e6ae19711fa
        val url = createUrl("session", "new")
        val requestBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("request_token", request_token)
            .build()
        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()
        return getDataToLogIn(url, "session_id", request)
    }

    private fun getDataToLogIn(url: String, objectNeeded: String, request: Request): String {
        val client: OkHttpClient = OkHttpClient()
        var toReturn = ""
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}
            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")
                    var rawData: String = response.body!!.string()
                    activity?.runOnUiThread {
                        toReturn = JSONObject(rawData).getString(objectNeeded).toString()

                        Log.d("__________$objectNeeded", "$toReturn")
                    }
                }
            }
        })
        return toReturn
    }

    fun showSnackbar(title: String,message:String, option: String, url:String) {
        progressB_login.visibility = View.GONE

        val snackbar = Snackbar.make(btnView!!, "", Snackbar.LENGTH_SHORT)
        val layout = snackbar.view as SnackbarLayout
        val textView=layout.findViewById<View>(R.id.snackbar_text) as TextView
        textView.visibility = View.INVISIBLE

        val snackView: View = layoutInflater.inflate(R.layout.snackbar, null)

        val imageView: ImageView = snackView.findViewById<View>(R.id.img_snack) as ImageView
        Glide.with(activity?.applicationContext!!).load(url).into(imageView)
        val lbltitle = snackView.findViewById<View>(R.id.lbl_snack) as TextView
        val lblmesage = snackView.findViewById<View>(R.id.lbl_snack2) as TextView
        lbltitle.setText(title)
        lblmesage.setText(message)

        val btn_snack:Button= snackView.findViewById<View>(R.id.btn_snack) as Button
        btn_snack.text=option
        btn_snack.setOnClickListener {
            txtPassword_logIn.setText("")
            txtPassword_logIn.clearFocus()
            txtUsername_logIn.setText("")
            txtUsername_logIn.clearFocus()
            snackbar.dismiss()
        }
        layout.addView(snackView, 0)
       snackbar.show()

/* snackbar.setAction(option) {
            txtPassword_logIn.setText("")
            txtPassword_logIn.clearFocus()
            txtUsername_logIn.setText("")
            txtUsername_logIn.clearFocus()
        }*/
        /* SIMPLE
    Snackbar.make(btnView!!,
         title, Snackbar.LENGTH_LONG)
         .setAction(option) {
             txtPassword_logIn.setText("")
             txtPassword_logIn.clearFocus()
             txtUsername_logIn.setText("")
             txtUsername_logIn.clearFocus()
         }.show()*/

        /*Con icono
        val snackbar: Snackbar = Snackbar.make(btnView!!, R.string.app_name, Snackbar.LENGTH_LONG)
         val snackbarLayout = snackbar.view
         val textView =
             snackbarLayout.findViewById<View>(R.id.snackbar_text) as TextView
         textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_menu_share, 0, 0, 0)
         textView.compoundDrawablePadding =
             resources.getDimensionPixelOffset(R.dimen.fab_margin)
         snackbar.show()*/
    }

    private fun validateAccess() {

        val url = createUrl("token", "new")
        val request = Request.Builder().url(url).build()
        request_token = getDataToLogIn(url, "request_token", request)
        val requestBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("username", txtUsername_logIn.text.toString())
            .addFormDataPart("password", txtPassword_logIn.text.toString())
            .addFormDataPart("request_token", request_token)
            .build()
        makePost(OkHttpClient(), createUrl("token", "validate_with_login"), requestBody, true)
    }

    private fun createUrl(type: String, field: String): String {
        val url = HttpUrl.Builder()
            .scheme("https")
            .host("api.themoviedb.org")
            .addPathSegment("3")
            .addPathSegment("authentication")
            .addPathSegment(type) //"token" or "session"
            .addPathSegment(field)
            .addQueryParameter("api_key", getString(R.string.api_key))
            .build()
        return url.toString()
    }

    private fun makePost(
        client: OkHttpClient,
        url: String,
        requestBody: RequestBody,
        isLogIn: Boolean
    ) {
        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()
        client.newCall(request).enqueue(
            object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Toast.makeText(activity, "Error trying to execute action", Toast.LENGTH_LONG)
                        .show()
                }

                override fun onResponse(call: Call, response: Response) {
                    var rawData: String = response.body!!.string()
                    response.use {
                        if (!response.isSuccessful) {
                            activity?.runOnUiThread {
                                if (isLogIn) failedLogIn() else failedLogOut()
                            }
                            throw IOException()
                        }
                        activity?.runOnUiThread {
                            if (isLogIn) {
                                request_token =
                                    JSONObject(rawData).getString("request_token").toString()
                                // session_id=getSessionId(request_token) se produce un error l descomentar esto.
                                successLogIn()
                            } else {
                                successLogOut()
                            }
                        }
                    }
                }
            }
        )
    }

    private fun failedLogIn() {
        val url="https://www.pngrepo.com/png/209049/512/suspicious-emoji.png"
        showSnackbar("Something is wrong....","Please check your data", "Try Again",url)
        //Toast.makeText(activity, "Please check your data", Toast.LENGTH_LONG).show()
        logInModel.setFlag(false)
        logInModel.setUser("")
    }

    private fun successLogIn() {
        val url="https://pngimg.com/uploads/welcome/welcome_PNG108.png"
        showSnackbar("Welcome Back!","Welcome ${txtUsername_logIn.text.toString()}", "Continue",url)

        //Toast.makeText(activity, "Welcome ${txtUsername_logIn.text.toString()}", Toast.LENGTH_LONG).show()
        logInModel.setFlag(true)
        logInModel.setUser(txtUsername_logIn.text.toString())
    }

    private fun failedLogOut() {
        //Wilmer, si lees esto, lo siento por la falta de seriedad...como ya habras notado me gusta poner muchas cosas asi
        /*  Toast.makeText(
              activity,
              "Looks like we can`t let you go :( ${logInModel.user.value}",
              Toast.LENGTH_LONG
          ).show()*/
        val url="https://i.dlpng.com/static/png/6368027_preview.png"
        showSnackbar("Something went wrong","Looks like we can`t let you go :( ${logInModel.user.value}", "Try Again",url)



    }

    private fun successLogOut() {
        val url="https://i.dlpng.com/static/png/245348_thumb.png"
        showSnackbar("See you later!!","See you next time ${logInModel.user.value}", "Continue",url)

        // Toast.makeText(activity, "See you next time ${logInModel.user.value}", Toast.LENGTH_LONG).show()
        logInModel.setFlag(false)
        logInModel.setUser("")
        txtUsername_logIn.text.clear()
        txtPassword_logIn.text.clear()
        logInModel.loadFavoriteList(ArrayList())
    }


}
