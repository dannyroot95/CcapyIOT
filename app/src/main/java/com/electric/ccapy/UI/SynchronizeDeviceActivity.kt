package com.electric.ccapy.UI

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.electric.ccapy.Models.Device
import com.electric.ccapy.Models.Users
import com.electric.ccapy.Providers.AuthProviders
import com.electric.ccapy.Utils.CaptureCodeBar
import com.electric.ccapy.Utils.Constants
import com.electric.ccapy.Utils.TinyDB
import com.electric.ccapy.databinding.ActivitySynchronizeDeviceBinding
import com.google.zxing.integration.android.IntentIntegrator

class SynchronizeDeviceActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySynchronizeDeviceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySynchronizeDeviceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imgScan.setOnClickListener {
            initScanner()
        }

    }

    private fun initScanner(){
        val integrator = IntentIntegrator(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
        integrator.setPrompt("Scaneando...")
        integrator.captureActivity = CaptureCodeBar::class.java
        integrator.setOrientationLocked(false)
        integrator.setBeepEnabled(true)
        integrator.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (scanResult != null) {
            if (scanResult.contents == null) {
                // Toast.makeText(this, "Cancelado", Toast.LENGTH_LONG).show()
            }else{
                val dbTiny = TinyDB(this)
                val objUser = TinyDB(this).getObject(Constants.USER, Users::class.java)
                val dataDevice = Device(scanResult.contents,objUser.id,objUser.fullname,System.currentTimeMillis())
                objUser.hasDevice = true
                dbTiny.putObject(Constants.USER,objUser)
                dbTiny.putString(Constants.ID_CHIP,scanResult.contents)
                AuthProviders().updateProfileDevice(objUser,dataDevice)
                startActivity(Intent(this,MenuActivity::class.java))
                finish()
            }
        }
    }
}

