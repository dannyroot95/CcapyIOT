package com.electric.ccapy.Services

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService

class StartAlertService {

   fun startService(switchLimit : Boolean , switchIt : Boolean , activity : Activity ){

        if(switchLimit || switchIt){
            if(!isActiveService(AlertsService::class.java,activity)){
                //NotificationManagerCompat.from(activity).cancelAll()
                activity.startService(Intent(activity.applicationContext, AlertsService::class.java))
            }
        }else{
            activity.stopService(Intent(activity.application, AlertsService::class.java))
            NotificationManagerCompat.from(activity).cancelAll()
        }

    }

    private fun isActiveService(myService : Class<AlertsService> , activity: Activity) : Boolean{

        val manager: ActivityManager = activity.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

        for(service : ActivityManager.RunningServiceInfo in
        manager.getRunningServices(Integer.MAX_VALUE)){
            if(myService.name.equals(service.service.className)){
                return true
            }
        }
        return false
    }

}