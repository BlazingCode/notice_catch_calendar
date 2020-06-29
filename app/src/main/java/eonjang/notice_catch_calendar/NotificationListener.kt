package eonjang.notice_catch_calendar

import android.annotation.SuppressLint
import android.app.Notification
import android.content.Intent
import android.graphics.drawable.Icon
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import androidx.annotation.RequiresApi

@SuppressLint("OverrideAbstract")
@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
class NotificationListener: NotificationListenerService() {
    var TAG = "NotificationListener"

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)
        var notification = sbn?.getNotification();
        var extras = sbn?.getNotification()?.extras;
        var title = extras?.getString(Notification.EXTRA_TITLE);
        var text = extras?.getCharSequence(Notification.EXTRA_TEXT);
        var subText = extras?.getCharSequence(Notification.EXTRA_SUB_TEXT);
        var smallIcon = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            notification?.getSmallIcon()
        } else {
            TODO("VERSION.SDK_INT < M")
        }
        var largeIcon = notification?.getLargeIcon();

        Log.d(TAG, "onNotificationPosted ~ " +
                " packageName: " + sbn?.getPackageName() +
                " id: " + sbn?.getId() +
                " postTime: " + sbn?.getPostTime() +
                " title: " + title +
                " text : " + text +
                " subText: " + subText);
    }

}
