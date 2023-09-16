package com.example.stopwatch


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.stopwatch.databinding.ActivityMainBinding
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding //for view binding
    private var isStarted = false

    private lateinit var serviceIntent: Intent //for service
    private var time = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //start stop click button
        binding.startbtn.setOnClickListener {
            startOrStop()
        }
        //reset button click
        binding.resetbtn.setOnClickListener {
            reset()
        }
        //intilize service intent
        serviceIntent = Intent(applicationContext, StopWatchService::class.java)
        registerReceiver(updateTime, IntentFilter(StopWatchService.update_time))

    }
//this function is responsible for start and stop funtion on start button click
    private fun startOrStop() {
        if (isStarted) {
            stop()//calling stop function
        } else {
            start()//calling start function
        }
    }
//starting stopwatch
    private fun start() {
        serviceIntent.putExtra(StopWatchService.current_time, time)//sending data to service class
        startService(serviceIntent)//satrt service
        binding.startbtn.text = "Stop"//set btn text
        isStarted = true

    }
// reset stopwatch
    private fun reset() {
        stop()//calling stop function
        time = 0.0
        binding.textView.text = getFormattedTime(time)//display formated time

    }
//stopping stopwatch
    private fun stop() {
        stopService(serviceIntent)//stop service
        binding.startbtn.text = "Start"//set btn text
        isStarted = false
    }
//broadcast receiver
    private val updateTime: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            //getting time from service
            time = intent!!.getDoubleExtra(StopWatchService.current_time, 0.0)
            binding.textView.text = getFormattedTime(time)//display formated time
        }
    }
//for getting formatted time
    private fun getFormattedTime(time: Double): String {
        val timeInt = time.roundToInt()
        val hours = timeInt % 86400 / 3600
        val minutes = timeInt % 86400 % 3600 / 60
        val sec = timeInt % 86400 % 3600 % 60
        return String.format("%02d:%02d:%02d", hours, minutes, sec)

    }
}