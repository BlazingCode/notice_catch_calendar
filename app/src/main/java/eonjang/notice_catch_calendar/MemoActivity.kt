package eonjang.notice_catch_calendar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.NumberPicker
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_memo.*

class MemoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memo)

        val intent = getIntent()
        var year = intent.getIntExtra("Year", 0)
        var month = intent.getIntExtra("Month", 0)
        var day = intent.getIntExtra("Day", 0)

        var start_hour = 0
        var start_minute = 0
        var finish_hour = 0
        var finish_minute = 0

        start_time_hour.minValue = 0
        start_time_hour.maxValue = 23
        start_time_minute.minValue = 0
        start_time_minute.maxValue = 59

        finish_time_hour.minValue = 0
        finish_time_hour.maxValue = 23
        finish_time_minute.minValue = 0
        finish_time_minute.maxValue = 59

        start_time_hour.setOnValueChangedListener { numberPicker, old, new ->
            start_hour = new
        }
        start_time_minute.setOnValueChangedListener { numberPicker, old, new ->
            start_minute = new
        }
        finish_time_hour.setOnValueChangedListener { numberPicker, old, new ->
            finish_hour = new
        }
        finish_time_minute.setOnValueChangedListener { numberPicker, old, new ->
            finish_minute = new
        }

        btn_cancle.setOnClickListener {
            finish()
        }

        btn_ok.setOnClickListener {
            Log.e("start_hour", start_hour.toString())
            Log.e("finish_hour", finish_hour.toString())
            if ((start_hour.toInt() < finish_hour.toInt()) && (start_minute < finish_minute)) {
                finish()
            }
            else{
                Toast.makeText(this, "시작 시간과 종료 시간을 확인해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
