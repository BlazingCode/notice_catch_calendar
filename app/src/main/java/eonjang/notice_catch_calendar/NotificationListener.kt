package eonjang.notice_catch_calendar

import android.annotation.SuppressLint
import android.app.Notification
import android.content.Context
import android.os.Build
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
        month = (dateInstance.get(Calendar.MONTH )+1).toString()
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

        while (keywordCusor.moveToNext())
            keywordSet.add(keywordCusor.getString(1))

        var extras = sbn?.getNotification()?.extras;
        var notiTitle = extras?.getString(Notification.EXTRA_TITLE).toString()
        var text = extras?.getCharSequence(Notification.EXTRA_TEXT).toString()
        var packageName = sbn?.getPackageName()

        year = dateInstance.get(Calendar.YEAR).toString()
        month = (dateInstance.get(Calendar.MONTH)+1).toString()
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
                        Log.d("Sucess", packageName + " " + text)
                        Toast.makeText(
                            this,
                            "어플 : "+packageName + "에서 보낸 "+ notiTitle + text+" 알림이 일정으로 등록되었습니다.",
                            Toast.LENGTH_LONG
                        ).show()
                        year = year.toInt().toString()
                        month = month.toInt().toString()
                        day = day.toInt().toString()
                        database.execSQL("INSERT INTO calendar('year','month','day','title','memo','start_time','finish_time','color') values('$year','$month','$day','$title','$memo','$start_time','$finish_time','$color'); ")
                        database.execSQL("INSERT INTO log('title','text')values('$notiTitle','$text');")
                    }
                } else {
                    Log.d("Sucess", packageName + " " + text)
                    Toast.makeText(
                        this,
                        "어플 : "+packageName + "에서 보낸 "+ notiTitle + text+" 알림이 일정으로 등록되었습니다.",
                        Toast.LENGTH_LONG
                    ).show()
                    year = year.toInt().toString()
                    month = month.toInt().toString()
                    day = day.toInt().toString()
                    database.execSQL("INSERT INTO calendar('year','month','day','title','memo','start_time','finish_time','color') values('$year','$month','$day','$title','$memo','$start_time','$finish_time','$color'); ")
                    database.execSQL("INSERT INTO log('title','text')values('$notiTitle','$text');")
                }
            }
        } else {
            if (findDate(notiTitle + " " + text)) {
                if (!(keywordSet.none())) {
                    if (findKeyword(notiTitle + " " + text)) {
                        Log.d("Sucess", packageName + " " + text)
                        Toast.makeText(
                            this,
                            "어플 : "+packageName + "에서 보낸 "+ notiTitle + text+" 알림이 일정으로 등록되었습니다.",
                            Toast.LENGTH_LONG
                        ).show()
                        year = year.toInt().toString()
                        month = month.toInt().toString()
                        day = day.toInt().toString()
                        database.execSQL("INSERT INTO calendar('year','month','day','title','memo','start_time','finish_time','color') values('$year','$month','$day','$title','$memo','$start_time','$finish_time','$color'); ")
                        database.execSQL("INSERT INTO log('title','text')values('$notiTitle','$text');")
                    }
                } else {
                    Log.d("Sucess", packageName + " " + text)
                    Toast.makeText(
                        this,
                        "어플 : "+packageName + "에서 보낸 "+ notiTitle + text+" 알림이 일정으로 등록되었습니다.",
                        Toast.LENGTH_LONG
                    ).show()
                    year = year.toInt().toString()
                    month = month.toInt().toString()
                    day = day.toInt().toString()
                    database.execSQL("INSERT INTO calendar('year','month','day','title','memo','start_time','finish_time','color') values('$year','$month','$day','$title','$memo','$start_time','$finish_time','$color'); ")
                    database.execSQL("INSERT INTO log('title','text')values('$notiTitle','$text');")
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
        val fullDateRegex = "(19|20)\\d{2}년(0[1-9]|[1-9]|1[012])월(0[1-9]|[1-9]|[12][0-9]|3[0-1])일".toRegex()
        val fullDateRegex2 = "\\d{2}년(0[1-9]|[1-9]|1[012])월(0[1-9]|[1-9]|[12][0-9]|3[0-1])일".toRegex()
        val fullDateRegex3 = "(19|20)\\d{2}.(0[1-9]|[1-9]|1[012]).(0[1-9]|[1-9]|[12][0-9]|3[0-1])".toRegex()
        val fullDateRegex4 = "\\d{2}.(0[1-9]|[1-9]|1[012]).(0[1-9]|[1-9]|[12][0-9]|3[0-1])".toRegex()
        val halfDateRegex = "(0[1-9]|[1-9]|1[012])월(0[1-9]|[1-9]|[12][0-9]|3[0-1])일".toRegex()
        val halfDateRegex2 = "(0[1-9]|[1-9]|1[012])[.](0[1-9]|[1-9]|[12][0-9]|3[0-1])".toRegex()
        val quarterDateRegex = "(0[1-9]|[1-9]|[12][0-9]|3[0-1])일".toRegex()
        val fullTimeRegex = "([1-9]|[01][0-9]|2[0-3]):([0-5][0-9])".toRegex()
        val fullTimeRegex2 = "([1-9]|[01][0-9]|2[0-3])시([0-5][0-9])분".toRegex()
        val hourRegex = "([1-9]|[01][0-9]|2[0-3])시".toRegex()

        when {
            !(fullDateRegex.find(notiString)?.value.equals(null)) -> {      //0000년 00월 00일 패턴이 알림에 포함될 경우
                flag = true
                var tmp = fullDateRegex.findAll(notiString)
                var date = tmp.last()!!.value.split("년", "월", "일")
                year=date[0]
                month = date[1]
                day = date[2]

            }
            !(fullDateRegex2.find(notiString)?.value.equals(null)) -> {  //00년 00월 00일 패턴이 알림에 포함될 경우
                flag = true
                var tmp = fullDateRegex2.findAll(notiString)
                var date = tmp.last()!!.value.split("년", "월", "일")
                year=year[0].toString()+ year[1].toString() +date[0]           //캘린더 일정 등록시 DB에 년도값이 xxxx같이 4자리여야 일정에 등록되므로 앞에 2자리 추가 100년 단위 일정은 안잡겠지,,, 21세기 말까지 서비스 운영할 생각도 없음
                month = date[1]
                day = date[2]
            }
            !(fullDateRegex3.find(notiString)?.value.equals(null)) -> { //0000.00.00 패턴이 알림에 포함될 경우 (년.월.일)
                flag = true
                var tmp = fullDateRegex3.findAll(notiString)
                var date = tmp.last()!!.value.split(".")
                year=date[0]
                month = date[1]
                day = date[2]
            }
            !(fullDateRegex4.find(notiString)?.value.equals(null)) -> { //00.00.00 패턴이 알림에 포함될 경우 (년,월.일)
                flag = true
                var tmp = fullDateRegex4.findAll(notiString)
                var date = tmp.last()!!.value.split(".")
                year=year[0].toString()+ year[1].toString() +date[0]          //캘린더 일정 등록시 DB에 년도값이 xxxx같이 4자리여야 일정에 등록되므로 앞에 2자리 추가  100년 단위 일정은 안잡겠지,,, 21세기 말까지 서비스 운영할 생각도 없음
                month = date[1]
                day = date[2]
            }
            !(halfDateRegex.find(notiString)?.value.equals(null)) -> {  //00월00일 패턴이 포함될경우
                flag = true
                var tmp = halfDateRegex.findAll(notiString)
                var date = tmp.last()!!.value.split("월", "일")
                month = date[0]
                day = date[1]
            }
            !(halfDateRegex2.find(notiString)?.value.equals(null)) -> { //00.00 패턴이 포함될경우 (월,일)
                flag = true
                var tmp = halfDateRegex2.findAll(notiString)
                var date = tmp.last()!!.value.split(".")
                month = date[0]
                day = date[1]
            }
            !(quarterDateRegex.find(notiString)?.value.equals(null)) -> {  //00일 패턴이 포함된경우
                flag = true
                var tmp = quarterDateRegex.findAll(notiString)
                var date = tmp.last()!!.value.replace("일", "")
                day = date
            }
            !(fullTimeRegex.find(notiString)?.value.equals(null)) -> {   //00:00 패턴이 포함된 경우 (00시00분)
                flag = true
                var tmp = fullTimeRegex.findAll(notiString)

                start_time = tmp.last()!!.value
                finish_time = tmp.last()!!.value

            }
            !(fullTimeRegex2.find(notiString)?.value.equals(null)) -> { //00시00분 패턴이 포함된 경우
                flag = true
                var tmp = fullTimeRegex2.findAll(notiString)
                start_time = tmp.last()!!.value.replace("시", ":").replace("분", "")
                finish_time = tmp.last()!!.value.replace("시", ":").replace("분", "")
            }
            !(hourRegex.find(notiString)?.value.equals(null)) -> { //00시 패턴이 포함된 경우
                flag = true
                var tmp = hourRegex.findAll(notiString)
                start_time =
                    tmp.last()!!.value.replace("시", ":") + "00"
                finish_time =
                    tmp.last()!!.value.replace("시", ":") + "00"
            }

        }
        return flag
    }


}


