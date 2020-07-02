package eonjang.notice_catch_calendar

import android.annotation.SuppressLint
import android.app.Notification
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Icon
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import java.util.*

@SuppressLint("OverrideAbstract")
@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
class NotificationListener : NotificationListenerService() {
    var TAG = "NotificationListener"
    lateinit var context: Context
    var appListSet = mutableSetOf<String>()
    var keywordSet = mutableSetOf<String>()

    val dateInstance = Calendar.getInstance() //현재 날짜
    lateinit var year: String
    lateinit var month: String
    lateinit var day: String
    lateinit var title: String
    lateinit var memo: String
    lateinit var start_time: String
    lateinit var finish_time: String
    lateinit var color: String

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        year = dateInstance.get(Calendar.YEAR).toString()
        month = dateInstance.get(Calendar.MONTH + 1).toString()
        day = dateInstance.get(Calendar.DATE).toString()
        title = "title"
        memo = "memo"
        start_time = "08:00"
        finish_time = "09:00"
        color = "-65536"
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)
        var notification = sbn?.getNotification()
        var dbHelper = DBHelper(context, "NoticeDB.db", null, 1)
        var database = dbHelper.writableDatabase

        var appListCusor = database.rawQuery(" SELECT * FROM appList", null)
        var keywordCusor = database.rawQuery(" SELECT * FROM keyword", null)

        while (appListCusor.moveToNext())
            appListSet.add(appListCusor.getString(1))

        while (appListCusor.moveToNext())
            keywordSet.add(keywordCusor.getString(1))

        var extras = sbn?.getNotification()?.extras;
        var notiTitle = extras?.getString(Notification.EXTRA_TITLE).toString()
        var text = extras?.getCharSequence(Notification.EXTRA_TEXT).toString()
        var packageName = sbn?.getPackageName()

        year = dateInstance.get(Calendar.YEAR).toString()
        month = dateInstance.get(Calendar.MONTH + 1).toString()
        day = dateInstance.get(Calendar.DATE).toString()
        title = notiTitle.toString()
        memo = text.toString()
        start_time = "08:00"
        finish_time = "09:00"
        color = "-65536"




        if (!(appListSet.none())) {
            if (appListSet.contains(packageName) && findDate(notiTitle + " " + text)) {
                if (!(keywordSet.none())) {
                    if (findKeyword(notiTitle + " " + text)) {
                        Log.d("Sucess",packageName+" "+text)
                        Toast.makeText(this,packageName+" "+" "+notiTitle+text,Toast.LENGTH_LONG).show()
                        year=year.toInt().toString()
                        month=month.toInt().toString()
                        day=day.toInt().toString()
                        database.execSQL("INSERT INTO calendar('year','month','day','title','memo','start_time','finish_time','color') values('$year','$month','$day','$title','$memo','$start_time','$finish_time','$color'); ")
                    }
                } else {
                    Log.d("Sucess",packageName+" "+text)
                    Toast.makeText(this,packageName+" "+" "+notiTitle+text,Toast.LENGTH_LONG).show()
                    year=year.toInt().toString()
                    month=month.toInt().toString()
                    day=day.toInt().toString()
                    database.execSQL("INSERT INTO calendar('year','month','day','title','memo','start_time','finish_time','color') values('$year','$month','$day','$title','$memo','$start_time','$finish_time','$color'); ")
                }
            }
        } else {
            if (findDate(notiTitle + " " + text)) {
                if (!(keywordSet.none())) {
                    if (findKeyword(notiTitle + " " + text)) {
                        Log.d("Sucess",packageName+" "+text)
                        Toast.makeText(this,packageName+" "+" "+notiTitle+text,Toast.LENGTH_LONG).show()
                        year=year.toInt().toString()
                        month=month.toInt().toString()
                        day=day.toInt().toString()
                        database.execSQL("INSERT INTO calendar('year','month','day','title','memo','start_time','finish_time','color') values('$year','$month','$day','$title','$memo','$start_time','$finish_time','$color'); ")
                    }
                } else {
                    Log.d("Sucess",packageName+" "+text)
                    Toast.makeText(this,packageName+" "+" "+notiTitle+text,Toast.LENGTH_LONG).show()
                    year=year.toInt().toString()
                    month=month.toInt().toString()
                    day=day.toInt().toString()
                    database.execSQL("INSERT INTO calendar('year','month','day','title','memo','start_time','finish_time','color') values('$year','$month','$day','$title','$memo','$start_time','$finish_time','$color'); ")
                }
            }
        }
    }

    fun findKeyword(notiString: String): Boolean {
        for (info in keywordSet)
            if (notiString.contains(info))
                return true

        return false
    }

    fun findDate(notiString: String): Boolean {
        var flag = false
        val fullDateRegex = "(19|20)\\d{2}년(0[1-9]|1[012])월(0[1-9]|[12][0-9]|3[0-1])일".toRegex()
        val fullDateRegex2 = "\\d{2}년(0[1-9]|1[012])월(0[1-9]|[12][0-9]|3[0-1])일".toRegex()
        val fullDateRegex3 = "(19|20)\\d{2}.(0[1-9]|1[012]).(0[1-9]|[12][0-9]|3[0-1])".toRegex()
        val fullDateRegex4 = "\\d{2}.(0[1-9]|1[012]).(0[1-9]|[12][0-9]|3[0-1])".toRegex()
        val halfDateRegex = "(0[1-9]|1[012])월(0[1-9]|[12][0-9]|3[0-1])일".toRegex()
        val halfDateRegex2 = "(0[1-9]|1[012]).(0[1-9]|[12][0-9]|3[0-1])".toRegex()
        val quarterDateRegex = "(0[1-9]|[12][0-9]|3[0-1])일".toRegex()
        val fullTimeRegex = "([1-9]|[01][0-9]|2[0-3]):([0-5][0-9])".toRegex()
        val fullTimeRegex2 = "([1-9]|[01][0-9]|2[0-3])시([0-5][0-9])분".toRegex()
        val hourRegex = "([1-9]|[01][0-9]|2[0-3])시".toRegex()

        if (!(fullDateRegex.find(notiString)?.value.equals(null))) {
            flag = true
            var tmp = fullDateRegex.findAll(notiString)
            var date = tmp.last()!!.value.split("년", "월", "일")
            month = date[1]
            day = date[2]
        }

        if (!(fullDateRegex2.find(notiString)?.value.equals(null))) {
            flag = true
            var tmp = fullDateRegex2.findAll(notiString)
            var date = tmp.last()!!.value.split("년", "월", "일")
            month = date[1]
            day = date[2]

        }


        if (!(fullDateRegex3.find(notiString)?.value.equals(null))) {
            flag = true
            var tmp = fullDateRegex3.findAll(notiString)
            var date = tmp.last()!!.value.split(".")
            month = date[1]
            day = date[2]

        }


        if (!(fullDateRegex4.find(notiString)?.value.equals(null))) {
            flag = true
            var tmp = fullDateRegex4.findAll(notiString)
            var date = tmp.last()!!.value.split(".")
            month = date[1]
            day = date[2]

        }

        if (!(halfDateRegex.find(notiString)?.value.equals(null))) {
            flag = true
            var tmp = halfDateRegex.findAll(notiString)
            var date = tmp.last()!!.value.split("월", "일")
            month = date[0]
            day = date[1]

        }

        if (!(halfDateRegex2.find(notiString)?.value.equals(null))) {
            flag = true
            var tmp = halfDateRegex2.findAll(notiString)
            var date = tmp.last()!!.value.split(".")
            month = date[0]
            day = date[1]

        }

        if (!(quarterDateRegex.find(notiString)?.value.equals(null))) {
            flag = true
            var tmp = quarterDateRegex.findAll(notiString)
            var date = tmp.last()!!.value.replace("일", "")
            day = date
        }

        if (!(fullTimeRegex.find(notiString)?.value.equals(null))) {
            flag = true
            var tmp = fullTimeRegex.findAll(notiString)

            start_time = tmp.last()!!.value
            finish_time = tmp.last()!!.value

        }

        if (!(fullTimeRegex2.find(notiString)?.value.equals(null))) {
            flag = true
            var tmp = fullTimeRegex2.findAll(notiString)
            start_time = tmp.last()!!.value.replace("시", ":").replace("분", "")
            finish_time = tmp.last()!!.value.replace("시", ":").replace("분", "")
        }

        if (!(hourRegex.find(notiString)?.value.equals(null))) {
            flag = true
            var tmp = hourRegex.findAll(notiString)
            start_time = tmp.last()!!.value.replace("시", ":") + "00"
            finish_time = tmp.last()!!.value.replace("시", ":") + "00"

        }

        return flag
    }
}


