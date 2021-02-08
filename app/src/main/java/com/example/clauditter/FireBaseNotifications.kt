package com.example.clauditter

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import com.example.clauditter.adapters.IS_LOGED
import com.example.clauditter.adapters.USERNAME
import com.example.clauditter.ui.clases.Movie
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


const val CHANNEL_ID="simlified_coding"
const val CHANNEL_NAME="simlified_coding"
const val CHANNEL_DESCRIPTION="simlified_coding"

class FireBaseNotifications:  FirebaseMessagingService(){

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        if(remoteMessage.notification!=null){
            val title=remoteMessage.notification!!.title
            val body=remoteMessage.notification!!.body
            if (body != null&&title!=null) {
                NotificationHelper().displayPushNotification(applicationContext,title,body)
            }
        }
    }
}


class NotificationHelper(){
    fun displayPushNotification(context:Context,title:String,body:String){
        val builder:NotificationCompat.Builder =
            NotificationCompat.Builder(context,CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_menu_gallery)
                .setContentTitle(title)
                .setContentText(body)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
         val notificatormanager:NotificationManagerCompat=NotificationManagerCompat.from(context)
        notificatormanager.notify(1,builder.build())
    }


    fun displayNotification(movieToShow:Movie,user:String,isLoged:Boolean, context: Context){
       val intent= Intent(context, Activity_MovieDetails::class.java)
        intent.putExtra(MOVIE_TRANSFER,movieToShow)
        intent.putExtra(USERNAME,user)
        intent.putExtra(IS_LOGED,isLoged)

         val pendingIntent:PendingIntent=PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT)

        val builder:NotificationCompat.Builder =
            NotificationCompat.Builder(context,CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_menu_gallery)
                .setTicker("$user this could be interesting for you") //mesage when app is comming
                .setContentTitle("Today`s Movie for you $user")
                .setSubText("SUBTEXTO")
                .setContentText("Movie: ${movieToShow.title}")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent)
        val notificatormanager:NotificationManagerCompat=NotificationManagerCompat.from(context)
        notificatormanager.notify(1,builder.build())

    }



}