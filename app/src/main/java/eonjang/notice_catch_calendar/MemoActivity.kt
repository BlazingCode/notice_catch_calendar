package eonjang.notice_catch_calendar

import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_memo.*
import petrov.kristiyan.colorpicker.ColorPicker
import petrov.kristiyan.colorpicker.ColorPicker.OnChooseColorListener

class MemoActivity : AppCompatActivity() {

    lateinit var dbHelper : DBHelper
    lateinit var database : SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memo)

        val intent = getIntent()
        var s_title : String = " "
        var s_color : String = " "
        var s_memo : String = " "
        var year = intent.getIntExtra("Year", 0)
        var month = intent.getIntExtra("Month", 0)
        var day = intent.getIntExtra("Day", 0)
        Log.e("year", year.toString())

        dbHelper = DBHelper(this, "newdb.db", null, 1)
        database = dbHelper.writableDatabase

        var start_hour = 0
        var start_minute = 0
        var finish_hour = 0
        var finish_minute = 0
        var start_time = "$start_hour:$start_minute"
        var finish_time = "$finish_hour:$finish_minute"

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

        btn_color.setOnClickListener {
            colorPicker()
        }

        btn_cancle.setOnClickListener {
            finish()
        }

        btn_ok.setOnClickListener {
            s_title = memo_title.text.toString()
            s_memo = memo.text.toString()
            start_time = "$start_hour:$start_minute"
            finish_time = "$finish_hour:$finish_minute"
            Log.e("start_hour", start_time)
            Log.e("finish_hour", finish_time)
            if ((start_hour.toString()+start_minute.toString()).toInt() <= (finish_hour.toString()+finish_minute.toString()).toInt()) {
                Log.e("year", year.toString())
                var query =
                    "INSERT INTO mytable('year','month','day','title','memo','start_time','finish_time','color') values('$year','$month','$day','$s_title','$s_memo','$start_time','$finish_time','$s_color');"
                database.execSQL(query)
                finish()
            }
            else{
                Toast.makeText(this, "시작 시간과 종료 시간을 확인해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun colorPicker(){
        var cp=ColorPicker(this)
        var colors = arrayListOf<String>("#000000", "#FFFFFF", "#00FF00", "#FF0000", "#0000FF")

        cp.setColors(colors) // 만들어둔 list 적용
            .setColumns(5) // 5열로 설정
            .setRoundColorButton(true) // 원형 버튼으로 설정
            .setOnChooseColorListener(object : OnChooseColorListener {
                override fun onChooseColor(position: Int, color: Int) {
                    btn_color.setBackgroundColor(color) // OK 버튼 클릭 시 이벤트
                }

                override fun onCancel() {
                    // Cancel 버튼 클릭 시 이벤트
                }
            }).show() // dialog 생성

    }
}
