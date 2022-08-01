package com.example.servicedemo

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat

class MediaService : Service() {

    private lateinit var mediaPlayer: MediaPlayer
    private val NOTIFICATION_CHANNEL_ID = "com.example.simpleapp"
    private val FOREGROUND_ID = 7

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground()
        mediaPlayer = MediaPlayer.create(this, R.raw.test)
        mediaPlayer.isLooping = true
        mediaPlayer.start()
        Toast.makeText(this, "Play music", Toast.LENGTH_SHORT).show()
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.stop()
        Toast.makeText(this, "Stop music", Toast.LENGTH_SHORT).show()
    }

    private fun startForeground() {
        val channelId =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel("my_service", "Media Service")
            } else {
                NOTIFICATION_CHANNEL_ID
            }

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
        val notification = notificationBuilder.setOngoing(true)
            .setContentTitle("Media Player Service")
            .setContentText("Music is now playing")
            .setTicker("Media Player Service Ticker")
            .setSmallIcon(R.drawable.ic_baseline_music_note_24)
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()
        startForeground(FOREGROUND_ID, notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId: String, channelName: String): String {
        val channel = NotificationChannel(
            channelId,
            channelName, NotificationManager.IMPORTANCE_HIGH
        )
        channel.lightColor = Color.BLUE
        channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(channel)
        return channelId
    }
}