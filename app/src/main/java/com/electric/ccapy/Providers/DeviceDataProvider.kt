package com.electric.ccapy.Providers

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import com.electric.ccapy.Models.DataDevice
import com.electric.ccapy.UI.MenuActivity
import com.electric.ccapy.Utils.Constants
import com.electric.ccapy.Utils.TinyDB
import com.electric.ccapy.databinding.ActivityMenuBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class DeviceDataProvider {

    var db : DatabaseReference = Firebase.database.reference
    var fs : FirebaseFirestore = FirebaseFirestore.getInstance()

    fun getData(activity : MenuActivity , binding : ActivityMenuBinding){
        val idChip = TinyDB(activity).getString(Constants.ID_CHIP).replace("\"","")

        db.child(Constants.DEVICES).child(idChip).child(Constants.CURRENT_DATA).addValueEventListener(object : ValueEventListener{
            @SuppressLint("SetTextI18n", "SimpleDateFormat")
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    val device = snapshot.getValue(DataDevice::class.java)!!
                    val ty = TinyDB(activity)
                    ty.putObject(Constants.CACHE_CURRENT_DATA,device)
                    ty.putString(Constants.KEY_CURRENT_DATA,Constants.STRING_CURRENT_DATA)
                    val sdfDate = SimpleDateFormat("dd/MM/yyyy hh:mm")
                    val netDate = Date(device.time*1000)
                    val myDate = sdfDate.format(netDate)
                    binding.txtTime.text = Constants.REGISTER_CURRENT_DATA+myDate
                    binding.txtEnergy.text = device.energy.toString()
                    binding.txtVoltage.text = device.voltage.toString()+Constants.METRIC_VOLTAGE
                    binding.txtWatts.text = device.watts.toString()+Constants.METRIC_POWER
                    binding.txtAmpere.text = device.ampere.toString()+Constants.METRIC_AMPERE
                    binding.txtFrecuency.text = device.frequency.toString()+Constants.METRIC_FREQUENCY
                    binding.lnLoader.visibility = View.GONE
                    binding.lnAllData.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

    }

}