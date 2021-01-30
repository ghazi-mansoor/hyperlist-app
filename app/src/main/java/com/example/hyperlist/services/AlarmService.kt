package com.example.hyperlist.services

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.PRIORITY_HIGH
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavDeepLinkBuilder
import com.example.hyperlist.MainActivity
import com.example.hyperlist.R
import com.example.hyperlist.ScanAlarmActivity

class AlarmService : Service() {

    private lateinit var mediaPlayer: MediaPlayer

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val alarmState = intent?.getStringExtra("alarm_state")

        if (alarmState == "alarm_on") {

            mediaPlayer = MediaPlayer.create(this, R.raw.alarm);
            mediaPlayer.start()

            val scanAlarmIntent = Intent(this, ScanAlarmActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(this, 0, scanAlarmIntent, PendingIntent.FLAG_CANCEL_CURRENT)

            val builder = NotificationCompat.Builder(this,"notChannel01").apply {
                setSmallIcon(R.drawable.ic_alarm_black_24dp)
                setContentTitle("Wake Up")
                setContentText("Tap this notification. Then scan a barcode to turn the alarm off.")
                setPriority(PRIORITY_HIGH)
                setContentIntent(pendingIntent)
                setAutoCancel(true)
            }

            with(NotificationManagerCompat.from(this)) {
                notify(100, builder.build())
            }

        }

        else if (alarmState == "alarm_off"){
            mediaPlayer.stop()
        }

        return START_NOT_STICKY

    }

}