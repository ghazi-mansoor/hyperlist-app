package com.example.hyperlist.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavDeepLinkBuilder
import com.example.hyperlist.R

class RemindersBroadCast : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        val pendingIntent = NavDeepLinkBuilder(context)
            .setGraph(R.navigation.main_nav_graph)
            .setDestination(R.id.timerMainFragment)
            .createPendingIntent()

        val builder = NotificationCompat.Builder(context,"notChannel02").apply {
            setSmallIcon(R.drawable.ic_notifications_active_black_24dp)
            setContentTitle("Get to work")
            setContentText("Time to start working on your next task for today")
            priority = NotificationCompat.PRIORITY_HIGH
            setContentIntent(pendingIntent)
            setAutoCancel(true)
        }

        with(NotificationManagerCompat.from(context)) {
            notify(100, builder.build())
        }

    }

}