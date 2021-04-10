package com.marvel.characters.util

import android.os.Handler
import android.os.SystemClock

class StopWatch(var onTimerUpdate:OnTimerUpdate) {

    fun resumeTimer(){
        startTime = System.currentTimeMillis() - timeWhenStopped
        timerHandler.postDelayed(timerRunnable, 0)
    }

    fun pauseTimer() {
        timeWhenStopped = System.currentTimeMillis() - startTime
        timerHandler.removeCallbacks(timerRunnable)
    }

    fun stopTimer() {
        timerHandler.removeCallbacks(timerRunnable)
    }

    var period = "00:00"
    //    var countUp = 0L
    var timeWhenStopped = 0L
    var startTime = 0L
    var timerHandler = Handler()
    lateinit var timerRunnable: Runnable
    fun startTimer() {
        timeWhenStopped = 0
        onTimerUpdate.onStart(period)

        startTime = SystemClock.elapsedRealtime()
        timerRunnable = Runnable {
            try {
                var currentPeriod = (SystemClock.elapsedRealtime() - startTime) / 1000
                var elapsedTime = StringBuilder()
                var minutes = (currentPeriod / 60)
                if (minutes in 0..9) {
                    elapsedTime.append("0$minutes")
                } else {
                    elapsedTime.append("$minutes")
                }
                elapsedTime.append(":")
                var seconds = currentPeriod % 60
                if (seconds in 0..9) {
                    elapsedTime.append("0$seconds")
                } else {
                    elapsedTime.append("$seconds")
                }
                period = elapsedTime.toString()
                onTimerUpdate.onUpdate(period)
                timerHandler.postDelayed(timerRunnable, 1000)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        timerHandler.postDelayed(timerRunnable, 0)
    }


    interface OnTimerUpdate {
        fun onStart(elapsedTimeInMinutes: String)
        fun onUpdate(elapsedTimeInMinutes: String)
    }
}