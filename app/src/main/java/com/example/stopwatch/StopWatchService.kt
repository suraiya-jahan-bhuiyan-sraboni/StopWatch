package com.example.stopwatch

import android.app.Service
import android.content.Intent
import android.os.IBinder
import java.util.Timer
import java.util.TimerTask


class StopWatchService: Service() {
    override fun onBind(intent: Intent?): IBinder? =null
    private val timer = Timer()
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val time=intent.getDoubleExtra(current_time,0.0)
        timer.scheduleAtFixedRate(StopWatchTimerTask(time),0,1000)
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        timer.cancel()
        super.onDestroy()
    }
    companion object{
        const val current_time="current time"
        const val update_time="updated time"
    }
    private inner class StopWatchTimerTask(private var time:Double):TimerTask(){
        override fun run() {
            val intent= Intent(update_time)
            time++
            intent.putExtra(current_time,time)
            sendBroadcast(intent)
        }

    }

}