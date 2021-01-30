package com.example.hyperlist.helpers

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import com.example.hyperlist.services.RemindersBroadCast
import java.util.*

object NotificationHelper {

    fun setNotificationTime(hours: Int, minutes: Int, meridiem: String, taskAlarmID: Int, context: Context, alarmManager: AlarmManager) {

        val intent = Intent(context, RemindersBroadCast::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, taskAlarmID, intent, PendingIntent.FLAG_CANCEL_CURRENT)

        val currentTime = System.currentTimeMillis()
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR, hours)
        cal.set(Calendar.MINUTE, minutes)
        if (meridiem == "AM") cal.set(Calendar.AM_PM, Calendar.AM)
        else cal.set(Calendar.AM_PM, Calendar.PM)

        var timeSet = cal.timeInMillis

        if (currentTime >= timeSet) {
            timeSet += 24 * 60 * 60 * 1000
        }

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, timeSet, AlarmManager.INTERVAL_DAY, pendingIntent)
    }

    fun cancelNotificationTime(taskAlarmID: Int, context: Context, alarmManager: AlarmManager) {
        val intent = Intent(context, RemindersBroadCast::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, taskAlarmID, intent, PendingIntent.FLAG_CANCEL_CURRENT)
        alarmManager.cancel(pendingIntent)
    }


}