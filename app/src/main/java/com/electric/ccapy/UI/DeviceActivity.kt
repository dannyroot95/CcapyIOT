package com.electric.ccapy.UI

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.electric.ccapy.Models.Device
import com.electric.ccapy.Models.Users
import com.electric.ccapy.Providers.AuthProviders
import com.electric.ccapy.Utils.CaptureCodeBar
import com.electric.ccapy.Utils.Constants
import com.electric.ccapy.Utils.TinyDB
import com.electric.ccapy.databinding.ActivityDeviceBinding
import com.google.zxing.integration.android.IntentIntegrator

class DeviceActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDeviceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeviceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()

        binding.btnRegister.setOnClickListener {
            startActivity(Intent(this,SynchronizeDeviceActivity::class.java))
        }


    }

    @SuppressLint("SetTextI18n")
    fun setupUI(){
        val chip = TinyDB(this).getString(Constants.ID_CHIP)
        binding.txtDeviceId.text = "ID de dispositivo : "+chip.replace("\"","")
    }

}