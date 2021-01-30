package com.example.hyperlist.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmBroadcast : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val alarmState = intent.getStringExtra("alarm_state")
        val alarmServiceIntent = Intent(context, AlarmService::class.java)
        alarmServiceIntent.putExtra("alarm_state", alarmState)
        context.startService(alarmServiceIntent)
    }
}