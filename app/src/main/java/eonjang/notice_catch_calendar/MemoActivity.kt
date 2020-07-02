package eonjang.notice_catch_calendar

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.NumberPicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import kotlinx.android.synthetic.main.activity_memo.*
import petrov.kristiyan.colorpicker.ColorPicker
import petrov.kristiyan.colorpicker.ColorPicker.OnChooseColorListener

class MemoActivity : AppCompatActivity() {

    lateinit var dbHelper : DBHelper
    lateinit var database : SQLiteDatabase
    var s_color : Int = -65536

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memo)

        btn_delete.visibility = View.VISIBLE

        dbHelper = DBHelper(this, "NoticeDB.db", null, 1)
        database = dbHelper.writableDatabase

        var start_time : String
        var finish_time : String
        var start_hour = 0
        var start_minute = 0
        var finish_hour = 0
        var finish_minute = 0
        var db_id : Int = 0
        var s_title : String = " "
        var s_memo : String = " "

        val intent = getIntent()

        var year = intent.getIntExtra("Year", 0)
        var month = intent.getIntExtra("Month", 0)
        var day = intent.getIntExtra("Day",0)
        var type = intent.getIntExtra("Type", 0)

        if(type == 1){      // 수정, 삭제
            db_id = intent.getIntExtra("DB_Id", 0)
            var query = "SELECT * FROM calendar WHERE _id = '$db_id'"
            var c = database.rawQuery(query,null)

            while(c.moveToNext()) {
                memo_title.setText(c.getString(c.getColumnIndex("title")))
                edit_memo.setText(c.getString(c.getColumnIndex("memo")))

                var start_time = c.getString(c.getColumnIndex("start_time"))
                var finish_time = c.getString(c.getColumnIndex("finish_time"))
                var arr1 = start_time.split(":")
                var arr2 = finish_time.split(":")
                start_hour = arr1[0].toInt()
                start_minute = arr1[1].toInt()
                finish_hour = arr2[0].toInt()
                finish_minute = arr2[1].toInt()

                var color = c.getInt(c.getColumnIndex("color"))
                btn_color.setBackgroundColor(color)
            }
        }
        else if(type == 2){         // 추가
            btn_delete.visibility = View.INVISIBLE
        }
        else{
            //error
        }


        Log.e("start_hour", start_hour.toString())

        Log.e("start_time_hour", start_time_hour.getValue().toString())


        start_time_hour.minValue = 0
        start_time_hour.maxValue = 23
        start_time_hour.value = start_hour

        start_time_minute.minValue = 0
        start_time_minute.maxValue = 59
        start_time_minute.value = start_minute

        finish_time_hour.minValue = 0
        finish_time_hour.maxValue = 23
        finish_time_hour.value = finish_hour


        finish_time_minute.minValue = 0
        finish_time_minute.maxValue = 59
        finish_time_minute.value = finish_minute

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

        btn_delete.setOnClickListener {
            var arr : Array<String> = arrayOf(db_id.toString())
            database.delete("calendar","_id=?", arr)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        btn_cancle.setOnClickListener {
            finish()
        }

        btn_ok.setOnClickListener {
            s_title = memo_title.text.toString()
            s_memo = edit_memo.text.toString()
            start_time = "$start_hour:$start_minute"
            finish_time = "$finish_hour:$finish_minute"
            Log.e("start_hour", start_time)
            Log.e("finish_hour", finish_time)

            if ((start_hour.toString()+start_minute.toString()).toInt() <= (finish_hour.toString()+finish_minute.toString()).toInt()) {
                Log.e("year", year.toString())
                if(type == 1){
                    var contentValues = ContentValues()
                    contentValues.put("title", "$s_title")
                    contentValues.put("memo", "$s_memo")
                    contentValues.put("start_time", "$start_time")
                    contentValues.put("finish_time", "$finish_time")
                    contentValues.put("color", "$s_color")

                    var arr : Array<String> = arrayOf("$db_id")

                    database.update("calendar", contentValues, "_id = ?", arr)

                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
                else if(type == 2){
                    var query =
                        "INSERT INTO calendar('year','month','day','title','memo','start_time','finish_time','color') values('$year','$month','$day','$s_title','$s_memo','$start_time','$finish_time','$s_color');"
                    database.execSQL(query)
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }

            }
            else{
                Toast.makeText(this, "시작 시간과 종료 시간을 확인해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun colorPicker(){
        var cp=ColorPicker(this)
        var colors = arrayListOf<String>("#FF0000", "#FFBB00", "#FFE400", "#ABF200", "#1FDA11", "#00D8FF", "#0055FF", "#6600FF", "#FF00DD", "#FF007F")

        cp.setColors(colors) // 만들어둔 list 적용
            .setColumns(5) // 5열로 설정
            .setRoundColorButton(true) // 원형 버튼으로 설정
            .setOnChooseColorListener(object : OnChooseColorListener {
                override fun onChooseColor(position: Int, color: Int) {
                    s_color = color
                    btn_color.setBackgroundColor(color) // OK 버튼 클릭 시 이벤트
                }

                override fun onCancel() {
                    // Cancel 버튼 클릭 시 이벤트
                }
            }).show() // dialog 생성

    }
}
