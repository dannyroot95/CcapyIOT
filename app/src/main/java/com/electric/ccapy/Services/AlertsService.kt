package com.electric.ccapy.Services

import android.annotation.SuppressLint
import android.app.*
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import android.app.NotificationManager
import com.electric.ccapy.Models.Config
import com.electric.ccapy.Models.DataDevice
import com.electric.ccapy.Providers.AuthProviders
import com.electric.ccapy.R
import com.electric.ccapy.UI.MenuActivity
import com.electric.ccapy.Utils.Constants
import com.electric.ccapy.Utils.TinyDB
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class AlertsService : Service(){

    val db : FirebaseFirestore = FirebaseFirestore.getInstance()
    val dr : DatabaseReference = Firebase.database.reference

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        createNotificacionChannel()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        showNotification()
        initAlerts()
        return START_STICKY
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("UnspecifiedImmutableFlag")
    private fun showNotification() {
        val notification = Notification
            .Builder(this,Constants.CHANNEL_LIMIT_KWH_ID)
            .setContentText(Constants.ACTIVE)
            .setSmallIcon(R.drawable.icon_cow)
            .build()
        startForeground(Constants.NOTIFICATION_LIMIT_KWH_ID,notification)
    }

    private fun createNotificacionChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val serviceChannel = NotificationChannel(
                Constants.CHANNEL_LIMIT_KWH_ID,
                Constants.SERVICE_KWH_CHANNEL_ID,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("UnspecifiedImmutableFlag", "MissingPermission")
    private fun initAlerts(){
        val config = TinyDB(this).getObject(Constants.CONFIG,Config::class.java)!!
        val idChip = TinyDB(this).getString(Constants.ID_CHIP).replace("\"","")
        val notificationId = 1234
        val notificationIntent = Intent(applicationContext,MenuActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(applicationContext,0,notificationIntent,0)
        val largeIcon = R.drawable.item_voltage
        if (config.notify_limit){
            dr.child(Constants.DEVICES).child(idChip).child(Constants.CURRENT_DATA).addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){

                        val device = snapshot.getValue(DataDevice::class.java)!!

                        db.collection(Constants.CONFIG).document(AuthProviders().getCurrentUserID()).get().addOnSuccessListener {snapshot ->
                            if(snapshot.exists()){
                                val params = snapshot.toObject(Config::class.java)!!
                                val limitDB = params.limit.toFloat()
                                val limitDevice = device.energy

                                if (limitDevice > limitDB){

                                    val notification = Notification
                                        .Builder(applicationContext,Constants.CHANNEL_LIMIT_KWH_ID)
                                        .setContentTitle(Constants.ALERT)
                                        .setContentText("Se esta excediendo el límite de energía!!")
                                        .setSmallIcon(R.drawable.icon_cow)
                                        .setLargeIcon(BitmapFactory.decodeResource(resources,largeIcon))
                                        .setPriority(Notification.PRIORITY_DEFAULT)
                                        .setContentIntent(pendingIntent)
                                        .build()
                                    with(NotificationManagerCompat.from(applicationContext)) {
                                        notify(notificationId, notification)
                                    }
                                }

                                if(config.notify_intelligent){
                                    if(config.type_home == "No Residencial"){

                                    }else{

                                    }
                                }

                            }
                        }

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        }
    }

}