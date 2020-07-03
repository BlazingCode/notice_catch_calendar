package eonjang.notice_catch_calendar

import android.content.Context
import android.database.Cursor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_log.*

class LogActivity :AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log)

        dbHelper=DBHelper(this,"NoticeDB.db",null,1)
        database=dbHelper.writableDatabase

        val listView = findViewById<ListView>(R.id.log_list)
        var mAdapter =logAdapter(this)
        listView.adapter=mAdapter

        logBackButton.setOnClickListener(){
            finish()
        }

    }
}

private class logAdapter(context: Context): BaseAdapter() {
    private val mContext: Context
    private var logTitleList:MutableList<String>
    private var logTextList:MutableList<String>
    var c: Cursor
    init{
        c= database.rawQuery(" SELECT * FROM log",null)
        mContext=context
        logTitleList= arrayListOf()
        logTextList= arrayListOf()

        while(c.moveToNext()) {
            logTitleList.add(c.getString(1))
            logTextList.add(c.getString(2))
        }
    }
    override fun getCount(): Int {
        return logTitleList.size
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    override fun getItem(position: Int): Any {
        return logTitleList[position]
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view : View

        val layoutInflater = LayoutInflater.from(mContext)
        var logTitle = logTitleList[position]
        var logText= logTextList[position]
        view= layoutInflater.inflate(R.layout.row_log,parent, false)

        var title=view.findViewById<TextView>(R.id.logTitle)
        var text=view.findViewById<TextView>(R.id.logText)
        var deleteIcon =view.findViewById<ImageButton>(R.id.deleteButton)

        title.text=logTitle
        text.text=logText

        deleteIcon.setOnClickListener(){
            database.execSQL("DELETE FROM log WHERE text = '$logText'")
            refresh()
            notifyDataSetChanged()
        }
        return view
    }
    fun refresh(){
        logTitleList.clear()
        logTextList.clear()

        c= database.rawQuery(" SELECT * FROM log",null)
        while(c.moveToNext()) {
            logTitleList.add(c.getString(1))
            logTextList.add(c.getString(2))
        }
        notifyDataSetChanged()
    }
}
