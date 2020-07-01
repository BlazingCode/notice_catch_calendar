package eonjang.notice_catch_calendar

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_notified_allow_setting.*


lateinit var list:List<ResolveInfo>

class NotifiedAllowSetting:AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notified_allow_setting)

        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        list = packageManager.queryIntentActivities(intent, 0)

      /*  for (info in list) {
            val appActivity = info.activityInfo.name
            val appPackageName = info.activityInfo.packageName
            val appName = info.loadLabel(packageManager).toString()
            val drawable = info.activityInfo.loadIcon(packageManager)
            Log.d(
                "TEST", "appName : " + appName + ", appActivity : "
                        + appActivity + ", appPackageName : " + appPackageName
            )

        }*/
        val listView = findViewById<ListView>(R.id.appList)
        var mAdapter =AppListAdapter(this)
        listView.adapter=mAdapter

        appSettingBackButton.setOnClickListener(){
            finish()
        }
        settingSave.setOnClickListener(){
            var appList = mAdapter.getList()
            var dbHelper = DBHelper(this,"NoticeDB.db",null,1)
            var database = dbHelper.writableDatabase
            database.execSQL("DELETE FROM appList")

            for(info in appList)
                database.execSQL("INSERT INTO appList('packageName') values('$info');")
            finish()
        }
        settingCancle.setOnClickListener()
        {
            finish()
        }
    }
}

private class AppListAdapter(context: Context):BaseAdapter() {
    private val mContext:Context
    private var checkState:MutableSet<String>
    var dbHelper :DBHelper
    var database :SQLiteDatabase
    var c :Cursor

    init{
        mContext=context
        checkState= mutableSetOf()
        dbHelper = DBHelper(mContext,"NoticeDB.db",null,1)
        database = dbHelper.writableDatabase
        c= database.rawQuery(" SELECT * FROM appList",null)
        while(c.moveToNext())
            checkState.add(c.getString(1))
    }
    override fun getCount(): Int {
        return list.size
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    override fun getItem(position: Int): Any {
        return list.get(position)
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view :View

        val layoutInflater = LayoutInflater.from(mContext)
        val appPackageName = list[position].activityInfo.packageName
        view= layoutInflater.inflate(R.layout.row_app_list,parent, false)
        var appSwitch=view.findViewById<Switch>(R.id.switch0)
        var appImage = view.findViewById<ImageView>(R.id.appImage)
        var appNameView = view.findViewById<TextView>(R.id.appName)


        if(checkState.contains(appPackageName))
            appSwitch?.setChecked(true)
        else
            appSwitch?.setChecked(false)

        appImage?.setImageDrawable(list[position].activityInfo.loadIcon(mContext.packageManager))
        appNameView?.text = list[position].loadLabel(mContext.packageManager).toString()
        appSwitch?.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked)
            {
                checkState.add(appPackageName)
            }else{
                checkState.remove(appPackageName)
            }
        }
        return view
    }
    fun getList():MutableSet<String>{
        return checkState
    }
}
