package com.example.hyperlist.fragments

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast

import com.example.hyperlist.R
import com.example.hyperlist.services.AlarmBroadcast
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class AlarmMainFragment : Fragment() {

    private lateinit var mContext: Context
    private lateinit var alarmManager: AlarmManager
    private lateinit var intent: Intent
    private lateinit var pendingIntent: PendingIntent
    private lateinit var sharedPrefs: SharedPreferences
    private val alarmActivePreferenceName = "alarmActive"
    private val alarmTimeHoursPreferenceName = "alarmTimeHours"
    private val alarmTimeMinutesPreferenceName = "alarmTimeMinutes"
    private var alarmTimeHoursString = ""
    private var alarmTimeMinutesString = ""
    private var alarmTimeMeridiemString = ""

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_alarm_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val setAlarmButton = view.findViewById<Button>(R.id.setAlarmButton)
        val alarmTimePicker = view.findViewById<TimePicker>(R.id.alarmTimePicker)
        val alarmActiveIndicator = view.findViewById<TextView>(R.id.alarmIndicatorTextView)

        createNotificationChannel()
        alarmManager = mContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val cal = Calendar.getInstance()

        sharedPrefs = mContext.getSharedPreferences(alarmActivePreferenceName, MODE_PRIVATE)
        val sharedPrefsEditor = sharedPrefs.edit()

        if (!sharedPrefs.getBoolean(alarmActivePreferenceName, false)) {
            alarmActiveIndicator.visibility = View.INVISIBLE
            alarmTimePicker.visibility = View.VISIBLE
            setAlarmButton.text = "Set Alarm"
        }
        else {
            alarmActiveIndicator.visibility = View.VISIBLE
            alarmTimePicker.visibility = View.GONE
            var alarmTimeHoursInt = sharedPrefs.getInt(alarmTimeHoursPreferenceName, 0)
            val alarmTimeMinutesInt = sharedPrefs.getInt(alarmTimeMinutesPreferenceName, 0)

            if (alarmTimeHoursInt == 0) {
                alarmTimeHoursString = "12"
                alarmTimeMeridiemString = "AM"
            }
            else if (alarmTimeHoursInt != 0 && alarmTimeHoursInt <= 9) {
                alarmTimeHoursString = "0$alarmTimeHoursInt"
                alarmTimeMeridiemString = "AM"
            } else if (alarmTimeHoursInt in 10..11) {
                alarmTimeHoursString = "$alarmTimeHoursInt"
                alarmTimeMeridiemString = "AM"
            } else if (alarmTimeHoursInt == 12){
                alarmTimeHoursString = "$alarmTimeHoursInt"
                alarmTimeMeridiemString = "PM"
            } else {
                alarmTimeHoursInt -= 12
                if (alarmTimeHoursInt != 0 && alarmTimeHoursInt <= 9) {
                    alarmTimeHoursString = "0$alarmTimeHoursInt"
                    alarmTimeMeridiemString = "PM"
                } else if (alarmTimeHoursInt in 10..11) {
                    alarmTimeHoursString = "$alarmTimeHoursInt"
                    alarmTimeMeridiemString = "PM"
                } else if (alarmTimeHoursInt == 0) {
                    alarmTimeHoursString = "12"
                    alarmTimeMeridiemString = "AM"
                }
            }

            if (alarmTimeMinutesInt <= 9) {
                alarmTimeMinutesString = "0$alarmTimeMinutesInt"
            } else {
                alarmTimeMinutesString = "$alarmTimeMinutesInt"
            }

            alarmActiveIndicator.text = "Wake up at $alarmTimeHoursString:$alarmTimeMinutesString $alarmTimeMeridiemString daily"
            setAlarmButton.text = "Cancel Alarm"
        }

        alarmTimePicker.setOnTimeChangedListener { _, hourOfDay, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hourOfDay)
            cal.set(Calendar.MINUTE, minute-2)
            sharedPrefsEditor.putInt(alarmTimeHoursPreferenceName, hourOfDay).apply()
            sharedPrefsEditor.putInt(alarmTimeMinutesPreferenceName, minute).apply()
        }

        setAlarmButton.setOnClickListener {

            val currentTime = System.currentTimeMillis()
            if (!sharedPrefs.getBoolean(alarmActivePreferenceName, false)) {
                var timeSet = cal.timeInMillis
                if (currentTime >= timeSet) {
                    timeSet += 24 * 60 * 60 * 1000
                }

                intent = Intent(mContext, AlarmBroadcast::class.java)
                intent.putExtra("alarm_state", "alarm_on")
                pendingIntent = PendingIntent.getBroadcast(mContext, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)

                var alarmTimeHoursInt = sharedPrefs.getInt(alarmTimeHoursPreferenceName, 0)
                val alarmTimeMinutesInt = sharedPrefs.getInt(alarmTimeMinutesPreferenceName, 0)

                if (alarmTimeHoursInt == 0) {
                    alarmTimeHoursString = "12"
                    alarmTimeMeridiemString = "AM"
                }
                else if (alarmTimeHoursInt != 0 && alarmTimeHoursInt <= 9) {
                    alarmTimeHoursString = "0$alarmTimeHoursInt"
                    alarmTimeMeridiemString = "AM"
                } else if (alarmTimeHoursInt in 10..11) {
                    alarmTimeHoursString = "$alarmTimeHoursInt"
                    alarmTimeMeridiemString = "AM"
                } else if (alarmTimeHoursInt == 12){
                    alarmTimeHoursString = "$alarmTimeHoursInt"
                    alarmTimeMeridiemString = "PM"
                } else {
                    alarmTimeHoursInt -= 12
                    if (alarmTimeHoursInt != 0 && alarmTimeHoursInt <= 9) {
                        alarmTimeHoursString = "0$alarmTimeHoursInt"
                        alarmTimeMeridiemString = "PM"
                    } else if (alarmTimeHoursInt in 10..11) {
                        alarmTimeHoursString = "$alarmTimeHoursInt"
                        alarmTimeMeridiemString = "PM"
                    } else if (alarmTimeHoursInt == 0) {
                        alarmTimeHoursString = "12"
                        alarmTimeMeridiemString = "AM"
                    }
                }

                if (alarmTimeMinutesInt <= 9) {
                    alarmTimeMinutesString = "0$alarmTimeMinutesInt"
                } else {
                    alarmTimeMinutesString = "$alarmTimeMinutesInt"
                }

                sharedPrefsEditor.putBoolean(alarmActivePreferenceName, true).apply()
                alarmActiveIndicator.text = "Wake up at $alarmTimeHoursString:$alarmTimeMinutesString $alarmTimeMeridiemString daily"

                alarmActiveIndicator.visibility = View.VISIBLE
                alarmTimePicker.visibility = View.GONE
                setAlarmButton.text = "Cancel Alarm"
                Toast.makeText(context, "Alarm time set", Toast.LENGTH_SHORT).show()
            }

            else {
                intent = Intent(mContext, AlarmBroadcast::class.java)
                intent.putExtra("alarm_state", "alarm_on")
                pendingIntent = PendingIntent.getBroadcast(mContext, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)
                alarmManager.cancel(pendingIntent)

                sharedPrefsEditor.putBoolean(alarmActivePreferenceName, false).apply()
                sharedPrefsEditor.remove(alarmTimeHoursPreferenceName).commit()
                sharedPrefsEditor.remove(alarmTimeMinutesPreferenceName).commit()

                alarmActiveIndicator.visibility = View.INVISIBLE
                alarmTimePicker.visibility = View.VISIBLE
                setAlarmButton.text = "Set Alarm"
                Toast.makeText(context, "Alarm cancelled", Toast.LENGTH_SHORT).show()
            }

        }

    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "simpleNotificationChannel"
            val descriptionText = "This channel is used to generate a simple notification"
            val importance =  NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("notChannel01", name, importance).apply {
                description = descriptionText
            }
            val notificationManager : NotificationManager = mContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

}
