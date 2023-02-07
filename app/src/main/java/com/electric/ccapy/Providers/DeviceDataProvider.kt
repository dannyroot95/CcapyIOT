package com.electric.ccapy.Providers

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.electric.ccapy.Models.*
import com.electric.ccapy.UI.MapsActivity
import com.electric.ccapy.UI.MenuActivity
import com.electric.ccapy.UI.SynchronizeDeviceActivity
import com.electric.ccapy.Utils.Constants
import com.electric.ccapy.Utils.Convert
import com.electric.ccapy.Utils.TinyDB
import com.electric.ccapy.databinding.ActivityMenuBinding
import com.google.android.gms.maps.GoogleMap
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
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

                    db.child(Constants.DEVICES).child(idChip).child(Constants.CONFIG).addListenerForSingleValueEvent(object : ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if(snapshot.exists()){
                                val configSnap =  snapshot.getValue(Config::class.java)!!
                                ty.putString(Constants.KEY_CONFIG_DATA,Constants.STRING_CONFIG_DATA)
                                ty.putObject(Constants.CONFIG,configSnap)
                                fs.collection(Constants.CONFIG).document(AuthProviders().getCurrentUserID()).set(configSnap)
                                fs.collection(Constants.CONFIG).document(AuthProviders().getCurrentUserID()).get().addOnSuccessListener {snapshot ->
                                    if(snapshot.exists()){
                                        val config = snapshot.toObject(Config::class.java)!!
                                        ty.putString(Constants.KEY_CONFIG_DATA,Constants.STRING_CONFIG_DATA)
                                        ty.putObject(Constants.CONFIG,config)

                                        db.child(Constants.DEVICES).child(idChip).child(Constants.CONFIG).get().addOnSuccessListener {
                                            if(it.exists()){
                                                binding.lnNoConfig.visibility = View.GONE
                                                binding.txtTotalC.text = Convert().twoDecimals(config.actual_med_read.toFloat()+device.energy)+" kw/h"
                                                if(config.actual_read == "0.0"){
                                                    binding.txtActualC.text = Convert().twoDecimals(device.energy)+" kw/h"
                                                }else{
                                                    binding.txtActualC.text = Convert().twoDecimals((config.actual_med_read.toFloat()-config.actual_read.toFloat())+device.energy)+" kw/h"
                                                }
                                                binding.txtTime.text = Constants.REGISTER_CURRENT_DATA+myDate
                                                binding.txtEnergy.text = device.energy.toString()
                                                binding.txtVoltage.text = device.voltage.toString()+Constants.METRIC_VOLTAGE
                                                binding.txtWatts.text = device.watts.toString()+Constants.METRIC_POWER
                                                binding.txtAmpere.text = device.ampere.toString()+Constants.METRIC_AMPERE
                                                binding.txtFrecuency.text = device.frequency.toString()+Constants.METRIC_FREQUENCY
                                                binding.lnLoader.visibility = View.GONE
                                                binding.lnAllData.visibility = View.VISIBLE
                                            }else{
                                                val configTiny = TinyDB(activity).getString(Constants.KEY_CONFIG_DATA)
                                                if(configTiny != ""){
                                                    val factory = TinyDB(activity).getObject(Constants.CONFIG,Config::class.java)
                                                    setConfigData(factory,activity)
                                                    binding.lnNoConfig.visibility = View.GONE
                                                    binding.txtTotalC.text = Convert().twoDecimals(config.actual_med_read.toFloat()+device.energy)+" kw/h"
                                                    if(config.actual_read == "0.0"){
                                                        binding.txtActualC.text = Convert().twoDecimals(device.energy)+" kw/h"
                                                    }else{
                                                        binding.txtActualC.text = Convert().twoDecimals((config.actual_med_read.toFloat()-config.actual_read.toFloat())+device.energy)+" kw/h"
                                                    }
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

                                        }

                                    }else{
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

                            }else{
                                fs.collection(Constants.CONFIG).document(AuthProviders().getCurrentUserID()).get().addOnSuccessListener {snapshot ->
                                    if(snapshot.exists()){
                                        val config = snapshot.toObject(Config::class.java)!!
                                        ty.putString(Constants.KEY_CONFIG_DATA,Constants.STRING_CONFIG_DATA)
                                        ty.putObject(Constants.CONFIG,config)

                                        db.child(Constants.DEVICES).child(idChip).child(Constants.CONFIG).get().addOnSuccessListener {
                                            if(it.exists()){
                                                binding.lnNoConfig.visibility = View.GONE
                                                binding.txtTotalC.text = Convert().twoDecimals(config.actual_med_read.toFloat()+device.energy)+" kw/h"
                                                if(config.actual_read == "0.0"){
                                                    binding.txtActualC.text = Convert().twoDecimals(device.energy)+" kw/h"
                                                }else{
                                                    binding.txtActualC.text = Convert().twoDecimals((config.actual_med_read.toFloat()-config.actual_read.toFloat())+device.energy)+" kw/h"
                                                }
                                                binding.txtTime.text = Constants.REGISTER_CURRENT_DATA+myDate
                                                binding.txtEnergy.text = device.energy.toString()
                                                binding.txtVoltage.text = device.voltage.toString()+Constants.METRIC_VOLTAGE
                                                binding.txtWatts.text = device.watts.toString()+Constants.METRIC_POWER
                                                binding.txtAmpere.text = device.ampere.toString()+Constants.METRIC_AMPERE
                                                binding.txtFrecuency.text = device.frequency.toString()+Constants.METRIC_FREQUENCY
                                                binding.lnLoader.visibility = View.GONE
                                                binding.lnAllData.visibility = View.VISIBLE
                                            }else{
                                                val configTiny = TinyDB(activity).getString(Constants.KEY_CONFIG_DATA)
                                                if(configTiny != ""){
                                                    val factory = TinyDB(activity).getObject(Constants.CONFIG,Config::class.java)
                                                    setConfigData(factory,activity)
                                                    binding.lnNoConfig.visibility = View.GONE
                                                    binding.txtTotalC.text = Convert().twoDecimals(config.actual_med_read.toFloat()+device.energy)+" kw/h"
                                                    if(config.actual_read == "0.0"){
                                                        binding.txtActualC.text = Convert().twoDecimals(device.energy)+" kw/h"
                                                    }else{
                                                        binding.txtActualC.text = Convert().twoDecimals((config.actual_med_read.toFloat()-config.actual_read.toFloat())+device.energy)+" kw/h"
                                                    }
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

                                        }

                                    }else{
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
                            }
                        }
                        override fun onCancelled(error: DatabaseError) {
                        }

                    })



                }else{
                    Toast.makeText(activity,"NO EXISTE EL DISPOSITIVO!",Toast.LENGTH_SHORT).show()
                    activity.startActivity(Intent(activity,SynchronizeDeviceActivity::class.java))
                    activity.finish()
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

    }

    fun setConfigData(dataX : Config , activity : Activity){
        val idChip = TinyDB(activity).getString(Constants.ID_CHIP).replace("\"","")
        fs.collection(Constants.CONFIG).document(AuthProviders().getCurrentUserID()).set(dataX)
        db.child(Constants.DEVICES).child(idChip).child(Constants.CONFIG).setValue(dataX)
        activity.startActivity(Intent(activity,MenuActivity::class.java))
    }


    fun setLocationDevice(context: Context,location : Location){
        val idChip = TinyDB(context).getString(Constants.ID_CHIP).replace("\"","")
        db.child(Constants.DEVICES).child(idChip).child(Constants.LOCATION).setValue(location)
    }

    fun getLocationDevice(context: MenuActivity) {
        val idChip = TinyDB(context).getString(Constants.ID_CHIP).replace("\"", "")
        db.child(Constants.DEVICES).child(idChip).child(Constants.LOCATION)
            .addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val location = snapshot.getValue(Location::class.java)!!
                        context.getLocationData(location)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }

    fun verifyCounterDevice(context: MenuActivity){
        val idChip = TinyDB(context).getString(Constants.ID_CHIP).replace("\"", "")
        db.child(Constants.DEVICES).child(idChip).child(Constants.CONFIG).child(Constants.COUNTER).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    val value = snapshot.value.toString().toInt()
                    context.verifyIsReloadMonth(value)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

}