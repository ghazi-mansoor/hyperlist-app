package com.example.hyperlist.fragments

import android.content.Context
import android.graphics.Color
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.navigation.findNavController

import com.example.hyperlist.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_timer_work.*

/**
 * A simple [Fragment] subclass.
 */
class TimerWorkFragment : Fragment() {

    private var initialTime : Long = 300000
    private var startTime : Long = 0
    private val timerInterval : Long = 1000
    private var started : Boolean = false
    private var paused : Boolean = false
    private var autoStart : Boolean = true
    private lateinit var timer : CountDownTimer
    private lateinit var mContext: Context

    private fun createTimer(start: Long, interval: Long, view: View): CountDownTimer {

        val timerText = view.findViewById<TextView>(R.id.timerTextView)

        val mediaPlayer = MediaPlayer()
        mediaPlayer.setAudioAttributes(
            AudioAttributes.Builder()
                .setFlags(AudioAttributes.FLAG_AUDIBILITY_ENFORCED)
                .setLegacyStreamType(AudioManager.STREAM_ALARM)
                .setUsage(AudioAttributes.USAGE_ALARM)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
        )
        mediaPlayer.setDataSource(
            mContext,
            Uri.parse("android.resource://" + mContext.getPackageName() + "/" + R.raw.tick))

        mediaPlayer.prepare()
        return object : CountDownTimer(start, interval) {

            override fun onTick(millisUntilFinished: Long) {

                startTime = millisUntilFinished
                mediaPlayer.start()
                val timeMinutes : Int = millisUntilFinished.toInt() / 60000
                val timeSeconds : Int = millisUntilFinished.toInt() % 60000
                val timeSecondsString : String = timeSeconds.toString()
                var timeSecondsSubString : String = ""
                if (timeSecondsString.length >= 5) timeSecondsSubString = timeSecondsString.substring(0,2)
                else timeSecondsSubString = "0${timeSecondsString.substring(0,1)}"
                timerText.text = "${timeMinutes}:${timeSecondsSubString}"

            }
            override fun onFinish() {
                mediaPlayer.stop()
                started = false
                paused = false
                view.findNavController().navigate(R.id.timerBreakFragment)
            }
        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_timer_work, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val timerControlButton = view.findViewById<FloatingActionButton>(R.id.timerControlButton)
        val timerSkipButton = view.findViewById<Button>(R.id.timerSkipButton)

        if (autoStart || (!started && !paused)) {
            timer = createTimer(initialTime, timerInterval, view)
            timer.start()
            started = true
            timerControlButton.setImageResource(R.drawable.ic_pause_white_24dp)
        }

        timerControlButton.setOnClickListener {
            if (started && !paused) {
                timer.cancel()
                paused = true
                started = false
                timerControlButton.setImageResource(R.drawable.ic_play_arrow_white_24dp)
            }

            else if (!started && paused) {
                timer = createTimer(startTime, timerInterval, view)
                timer.start()
                started = true
                paused = false
                timerControlButton.setImageResource(R.drawable.ic_pause_white_24dp)
            }

        }

        timerSkipButton.setOnClickListener {
            view.findNavController().navigate(R.id.timerBreakFragment)
        }

    }

    override fun onPause() {
        super.onPause()
        timer.cancel()
    }


}
