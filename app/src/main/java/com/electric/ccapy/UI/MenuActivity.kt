package com.electric.ccapy.UI

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.electric.ccapy.Models.DataDevice
import com.electric.ccapy.Models.Users
import com.electric.ccapy.Providers.AuthProviders
import com.electric.ccapy.Providers.DeviceDataProvider
import com.electric.ccapy.Utils.Constants
import com.electric.ccapy.Utils.CurrentDateTime
import com.electric.ccapy.Utils.TinyDB
import com.electric.ccapy.databinding.ActivityMenuBinding
import com.electric.ccapy.databinding.DialogOptionsBinding
import java.text.SimpleDateFormat
import java.util.*

class MenuActivity : AppCompatActivity() {
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

    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    private fun setupUI(){
        val st = TinyDB(this).getString(Constants.KEY_CURRENT_DATA)
        val sdfDate = SimpleDateFormat("dd/MM/yyyy")
        val netDate = Date(System.currentTimeMillis())
        val myDate = sdfDate.format(netDate).split("/")
        val chip = TinyDB(this).getString(Constants.ID_CHIP)
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
            DeviceDataProvider().getData(this,binding)
        }else{
            DeviceDataProvider().getData(this,binding)
        }


    }
    override fun onBackPressed() {
    }

}