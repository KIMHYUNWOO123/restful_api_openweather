package com.example.restful_api_practice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView.text = ""
        //쓰레드 동작
        button_seoul.setOnClickListener {
            textView.text = ""
            val thread = NetworkThread("seoul")
            thread.start()
            thread.join()

        }
        button_suwon.setOnClickListener {
            textView.text = ""
            val thread = NetworkThread("suwon")
            thread.start()
            thread.join()
        }
    }

    inner class NetworkThread(data:String) : Thread(){
        val data = data
        override fun run() {
            val region = data
            val key = "218c73d60692af6965fa11c043c3bf2d"

            val site = "http://api.openweathermap.org/data/2.5/weather?q=${region}&APPID=${key}&lang=kr&units=metric"
            val url = URL(site)
            val conn = url.openConnection()
            val input = conn.getInputStream()
            val isr = InputStreamReader(input)
            val br = BufferedReader(isr)

            var str : String? = null
            val buf = StringBuffer()

            do {
                str = br.readLine()

                if(str!=null){
                    buf.append(str)
                }
            }while (str!=null)

            val root = JSONObject(buf.toString())
            val weather = root.getJSONArray("weather")
            val main = root.getJSONObject("main")
            val weather_list = weather.getJSONObject(0)
            runOnUiThread {
                    textView.append(" ${weather_list.getString("main")}\n")
                    textView.append("오늘의 날씨는 ${weather_list.getString("description")}\n")
                    textView.append("오늘의 온도는 ${main.getString("temp")}\n")
                    textView.append("오늘의 습도는 ${main.getString("humidity")}\n")
                    textView.append("오늘의 기압은 ${main.getString("pressure")}\n")
                }
            }
        }

    }