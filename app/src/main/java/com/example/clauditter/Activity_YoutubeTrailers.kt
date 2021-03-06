package com.example.clauditter

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.clauditter.adapters.TRAILER_KEY
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView

class Activity_YoutubeTrailers : YouTubeBaseActivity(),YouTubePlayer.OnInitializedListener {
    private var videoKey="eFTLKWw542g"
    val playerView by lazy {YouTubePlayerView(this)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_youtube_trailers)

        val layout=layoutInflater.inflate(R.layout.activity_you_tube,null) as ConstraintLayout
        setContentView(layout)

        playerView.layoutParams=ConstraintLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        videoKey=intent.getStringExtra(TRAILER_KEY)!!
        layout.addView(playerView)
        playerView.initialize(getString(R.string.yt_api_key),this)
    }

    override fun onInitializationSuccess(
        provider: YouTubePlayer.Provider?,
        youtubePlayer: YouTubePlayer?,
        wasRestored: Boolean
    ) {
        /*
        if(!wasRestored){
            youtubePlayer?.cueVideo(videoKey)
        }*/
        youtubePlayer?.setPlayerStateChangeListener(playerStateChangeListener)
        youtubePlayer?.setPlaybackEventListener(playbackEventListener)
        if(!wasRestored){
            youtubePlayer?.loadVideo(videoKey) //reproducimos el video
        }else{
            youtubePlayer?.play()
        }

    }

    override fun onInitializationFailure(
        p0: YouTubePlayer.Provider?,
        p1: YouTubeInitializationResult?
    ) {
        Toast.makeText(this,"there has been and error loading the video ;(",Toast.LENGTH_SHORT).show()

    }



    //__________________________EVENT LISTENER________________________________
    private val playbackEventListener= object : YouTubePlayer.PlaybackEventListener{
        override fun onSeekTo(p0: Int) {}
        override fun onBuffering(p0: Boolean) {}
        override fun onPlaying() {}
        override fun onStopped() {}
        override fun onPaused() {}
    }

    //__________________________STATE CHANGE LISTENER________________________________
    private val playerStateChangeListener= object : YouTubePlayer.PlayerStateChangeListener{
        override fun onAdStarted() {}
        override fun onLoading() {}
        override fun onVideoStarted() {}
        override fun onLoaded(p0: String?) {}
        override fun onVideoEnded() {}
        override fun onError(p0: YouTubePlayer.ErrorReason?) {}
    }

}