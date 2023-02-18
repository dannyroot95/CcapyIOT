package com.electric.ccapy.UI

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.location.LocationListener
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import com.electric.ccapy.Models.Config
import com.electric.ccapy.Models.DataDevice
import com.electric.ccapy.Models.Location
import com.electric.ccapy.Models.Users
import com.electric.ccapy.Providers.AuthProviders
import com.electric.ccapy.Providers.DeviceDataProvider
import com.electric.ccapy.Services.AlertsService
import com.electric.ccapy.Services.StartAlertService
import com.electric.ccapy.Utils.Constants
import com.electric.ccapy.Utils.Convert
import com.electric.ccapy.Utils.CurrentDateTime
import com.electric.ccapy.Utils.TinyDB
import com.electric.ccapy.databinding.ActivityMenuBinding
import com.electric.ccapy.databinding.DialogOptionsBinding
import java.text.SimpleDateFormat
import java.util.*

class MenuActivity : AppCompatActivity() , LocationListener {
    private lateinit var binding : ActivityMenuBinding
    private lateinit var optionsBinding : DialogOptionsBinding
    private lateinit var dialog: Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        optionsBinding = DialogOptionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dialog = Dialog(this)
        dialog.window?.setBackgroundDrawable(ColorDrawable(0))
        dialog.setContentView(optionsBinding.root)

        setupUI()

        binding.btnProfile.setOnClickListener {
            startActivity(Intent(this,ProfileActivity::class.java))
        }
        binding.btnOptions.setOnClickListener {
            dialog.show()
            dialog.setCancelable(false)
            val window: Window = dialog.window!!
            window.setLayout(950, 1400)
        }
        binding.btnConfig.setOnClickListener {
            startActivity(Intent(this,ConfigActivity::class.java))
        }
        optionsBinding.closeDialog.setOnClickListener {
            dialog.dismiss()
        }
        optionsBinding.cvLogout.setOnClickListener {
            AuthProviders().logout(this)
        }
        optionsBinding.cvDevice.setOnClickListener {
            startActivity(Intent(this,DeviceActivity::class.java))
        }
        optionsBinding.cvConfig.setOnClickListener {
            startActivity(Intent(this,ConfigActivity::class.java))
        }
        optionsBinding.cvMetrics.setOnClickListener {
            startActivity(Intent(this,MetricsActivity::class.java))
        }
        optionsBinding.cvLocation.setOnClickListener {
            if(TinyDB(this).getObject(Constants.LOCATION,Location::class.java) != null){
                startActivity(Intent(this,MapsActivity::class.java))
            }else{
                Toast.makeText(this,"No disponible!",Toast.LENGTH_SHORT).show()
            }
        }
        optionsBinding.cvRegisters.setOnClickListener {
            startActivity(Intent(this,RegistersActivity::class.java))
        }
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    private fun setupUI(){
        val st = TinyDB(this).getString(Constants.KEY_CURRENT_DATA)
        val cf = TinyDB(this).getString(Constants.KEY_CONFIG_DATA)
        val userCappy = TinyDB(this).getObject(Constants.USER, Users::class.java)!!
        val sdfDate = SimpleDateFormat("dd/MM/yyyy")
        val netDate = Date(System.currentTimeMillis())
        val myDate = sdfDate.format(netDate).split("/")
        val chip = TinyDB(this).getString(Constants.ID_CHIP)
        val tittleName = userCappy.fullname.split(" ")
        binding.txtNameTitle.text = "HOLA, "+tittleName[tittleName.size-2]+" "+tittleName[tittleName.size-1]+"!"
        binding.txtCurrentDate.text = CurrentDateTime().getMonth(myDate[1])+" "+myDate[0]+", "+myDate[2]
        binding.txtChip.text = chip.replace("\"","")

        if(st != ""){
            binding.lnLoader.visibility = View.GONE
            binding.lnAllData.visibility = View.VISIBLE
            val db = TinyDB(this).getObject(Constants.CACHE_CURRENT_DATA,DataDevice::class.java)!!
            val sdfDate2 = SimpleDateFormat("dd/MM/yyyy hh:mm")
            val netDate2 = Date(db.time*1000)
            val myDate2 = sdfDate2.format(netDate2)
            binding.txtTime.text = Constants.REGISTER_CURRENT_DATA+myDate2
            binding.txtEnergy.text = db.energy.toString()
            binding.txtVoltage.text = db.voltage.toString()+Constants.METRIC_VOLTAGE
            binding.txtWatts.text = db.watts.toString()+Constants.METRIC_POWER
            binding.txtAmpere.text = db.ampere.toString()+Constants.METRIC_AMPERE
            binding.txtFrecuency.text = db.frequency.toString()+Constants.METRIC_FREQUENCY
            binding.txtPf.text = Constants.POWER_FACTOR_TITLE+db.power_factor.toString()

            if(cf != ""){
                val config = TinyDB(this).getObject(Constants.CONFIG,Config::class.java)!!
                binding.lnNoConfig.visibility = View.GONE
                binding.txtTotalC.text = Convert().twoDecimals(config.actual_med_read.toFloat()+db.energy)+" kw/h"
                if(config.actual_read == "0.0"){
                    binding.txtActualC.text = Convert().twoDecimals(db.energy)+" kw/h"
                }else{
                    binding.txtActualC.text = Convert().twoDecimals((config.actual_med_read.toFloat()-config.actual_read.toFloat())+db.energy)+" kw/h"
                }

                if(config.notify_limit || config.notify_intelligent){
                    StartAlertService().startService(config.notify_limit,config.notify_intelligent,this)
                }

            }

            DeviceDataProvider().getData(this,binding)
            DeviceDataProvider().getLocationDevice(this)
            DeviceDataProvider().verifyCounterDevice(this)
        }else{
            DeviceDataProvider().getData(this,binding)
            DeviceDataProvider().getLocationDevice(this)
            DeviceDataProvider().verifyCounterDevice(this)
        }


    }
    fun getLocationData(location : Location){
        val locationTiny = TinyDB(this)
        locationTiny.putObject(Constants.LOCATION,location)
    }

    fun verifyIsReloadMonth(value : Int){
        if(value == 1){
            val dataX = TinyDB(this)
            val config = dataX.getObject(Constants.CONFIG,Config::class.java)!!
            val newT = binding.txtTotalC.text.split(" ")[0]
            val newMonth = (0.0).toString()
            config.actual_med_read = newT
            config.actual_read = newMonth
            dataX.putObject(Constants.CONFIG,config)
            DeviceDataProvider().setConfigData(config,this)
        }
    }

    override fun onBackPressed() {
    }

    override fun onLocationChanged(p0: android.location.Location) {
        TODO("Not yet implemented")
    }


}