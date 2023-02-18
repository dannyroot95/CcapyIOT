package com.electric.ccapy.Services

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import com.electric.ccapy.MainActivity

class AutoStartReceiver : BroadcastReceiver() {
    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(p0: Context?, p1: Intent?) {
            if (p1!!.action.equals(Intent.ACTION_BOOT_COMPLETED)) {
                val activityIntent = Intent(p0!!, MainActivity::class.java)
                activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                p0.startActivity(activityIntent)
            }
        }
    }

