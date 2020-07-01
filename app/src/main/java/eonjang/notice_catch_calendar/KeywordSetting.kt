package eonjang.notice_catch_calendar

import android.content.Context
import android.content.pm.ResolveInfo
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_keyword_setting.*

lateinit var dbHelper :DBHelper
lateinit var database :SQLiteDatabase

class KeywordSetting :AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_keyword_setting)

        dbHelper=DBHelper(this,"NoticeDB.db",null,1)
        database=dbHelper.writableDatabase

        val listView = findViewById<ListView>(R.id.keyword_list)
        var mAdapter =keywordAdapter(this)
        listView.adapter=mAdapter

        keywordBackButton.setOnClickListener(){
            finish()
        }

        keywordSave.setOnClickListener(){
            var input=input_keyword.text.toString()
            Log.d("insert",input.length.toString())
            if(input.length==0) {
                Toast.makeText(this,"키워드를 입력해 주세요",Toast.LENGTH_SHORT).show()
            }else{
                database.execSQL("INSERT INTO keyword('word') values('$input');")
                input_keyword.text=null
                mAdapter.refresh()
            }

        }


    }
}

private class keywordAdapter(context: Context): BaseAdapter() {
    private val mContext: Context
    private var keywordList:MutableList<String>
    var c: Cursor
    init{
        c= database.rawQuery(" SELECT * FROM keyword",null)
        mContext=context
        keywordList= arrayListOf()

        while(c.moveToNext())
            keywordList.add(c.getString(1))
    }
    override fun getCount(): Int {
        return keywordList.size
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    override fun getItem(position: Int): Any {
        return keywordList[position]
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view : View

        val layoutInflater = LayoutInflater.from(mContext)
        var word = keywordList[position]
        view= layoutInflater.inflate(R.layout.row_keyword,parent, false)

        var text=view.findViewById<TextView>(R.id.keyword)
        var deleteIcon =view.findViewById<ImageButton>(R.id.deleteButton)
        text.text=word
        deleteIcon.setOnClickListener(){
            database.execSQL("DELETE FROM keyword WHERE word = '$word'")
            refresh()
            notifyDataSetChanged()
        }
        return view
    }
    fun refresh(){
        keywordList.clear()
        c= database.rawQuery(" SELECT * FROM keyword",null)
        while(c.moveToNext())
            keywordList.add(c.getString(1))
        notifyDataSetChanged()
    }
}
