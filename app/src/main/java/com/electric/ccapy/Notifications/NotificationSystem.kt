package com.electric.ccapy.Notifications

import android.annotation.SuppressLint
import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationManagerCompat
import com.electric.ccapy.R
import com.electric.ccapy.UI.MenuActivity
import com.electric.ccapy.Utils.Constants

class NotificationSystem {

    private val largeIconVoltage = R.drawable.item_voltage
    private val largeIcon30kwh = R.drawable.kwh30
    private val largeIcon140kwh = R.drawable.kwh140
    private val notificationIdLimit = 1234
    private val notificationIdLimit30 = 30
    private val notificationIdLimit140 = 140


    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.O)
    fun limitNotification(applicationContext : Context){
        val notificationIntent = Intent(applicationContext, MenuActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(applicationContext,0,notificationIntent,0)
        val notification = Notification
            .Builder(applicationContext, Constants.CHANNEL_LIMIT_KWH_ID)
            .setContentTitle(Constants.ALERT)
            .setContentText("Se esta excediendo el límite de energía!!")
            .setSmallIcon(R.drawable.icon_cow)
            .setLargeIcon(BitmapFactory.decodeResource(ContextWrapper(applicationContext).resources,largeIconVoltage))
            .setPriority(Notification.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .build()
        with(NotificationManagerCompat.from(applicationContext)) {
            notify(notificationIdLimit, notification)
        }
    }

    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.O)
    fun exceedEnergy30(applicationContext : Context){
        val notificationIntent = Intent(applicationContext, MenuActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(applicationContext,0,notificationIntent,0)
        val notification = Notification
            .Builder(applicationContext, Constants.CHANNEL_LIMIT_KWH_ID)
            .setContentTitle(Constants.EXCEED)
            .setContentText("Energia > 30kwh detectada!!")
            .setSmallIcon(R.drawable.icon_cow)
            .setLargeIcon(BitmapFactory.decodeResource(ContextWrapper(applicationContext).resources,largeIcon30kwh))
            .setPriority(Notification.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .build()
        with(NotificationManagerCompat.from(applicationContext)) {
            notify(notificationIdLimit30, notification)
        }
    }

    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.O)
    fun exceedEnergy140(applicationContext : Context){
        val notificationIntent = Intent(applicationContext, MenuActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(applicationContext,0,notificationIntent,0)
        val notification = Notification
            .Builder(applicationContext, Constants.CHANNEL_LIMIT_KWH_ID)
            .setContentTitle(Constants.EXCEED)
            .setContentText("Energia > 140kwh detectada!!")
            .setSmallIcon(R.drawable.icon_cow)
            .setLargeIcon(BitmapFactory.decodeResource(ContextWrapper(applicationContext).resources,largeIcon140kwh))
            .setPriority(Notification.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .build()
        with(NotificationManagerCompat.from(applicationContext)) {
            notify(notificationIdLimit140, notification)
        }
    }

}