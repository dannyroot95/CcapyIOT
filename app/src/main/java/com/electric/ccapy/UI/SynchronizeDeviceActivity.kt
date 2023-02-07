package com.electric.ccapy.UI

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.electric.ccapy.Models.Device
import com.electric.ccapy.Models.Users
import com.electric.ccapy.Providers.AuthProviders
import com.electric.ccapy.Providers.DeviceDataProvider
import com.electric.ccapy.Utils.CaptureCodeBar
import com.electric.ccapy.Utils.Constants
import com.electric.ccapy.Utils.TinyDB
import com.electric.ccapy.databinding.ActivitySynchronizeDeviceBinding
import com.google.zxing.integration.android.IntentIntegrator


class SynchronizeDeviceActivity : AppCompatActivity() , LocationListener {

    private lateinit var binding : ActivitySynchronizeDeviceBinding
    private lateinit var locationManager: LocationManager
    private val locationPermissionCode = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySynchronizeDeviceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getLocation()
        binding.imgScan.setOnClickListener {
            initScanner()
        }

    }

    private fun initScanner(){
        val integrator = IntentIntegrator(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
        integrator.setPrompt("Escaneando...")
        integrator.captureActivity = CaptureCodeBar::class.java
        integrator.setOrientationLocked(false)
        integrator.setBeepEnabled(true)
        integrator.initiateScan()
    }

    @Deprecated("Deprecated in Java")
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
                dbTiny.remove(Constants.ID_CHIP)
                dbTiny.remove(Constants.KEY_CURRENT_DATA)
                objUser.hasDevice = true
                dbTiny.putObject(Constants.USER,objUser)
                dbTiny.putString(Constants.ID_CHIP,scanResult.contents)
                AuthProviders().updateProfileDevice(objUser,dataDevice)
                startActivity(Intent(this,MenuActivity::class.java))
                finish()
            }
        }
    }

    private fun getLocation() {
        if (checkPermissions()){
            if (isLocationEnabled()){
                locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
                if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), locationPermissionCode)
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)

            }else{
                Toast.makeText(this,"Active su GPS!",Toast.LENGTH_SHORT).show()
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
        }else{
            requestPermission()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation()
            }
            else {
                Toast.makeText(this, "Denegado!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isLocationEnabled() : Boolean {
        val locationManagerActive = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManagerActive.isProviderEnabled(LocationManager.GPS_PROVIDER)||locationManagerActive.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun checkPermissions() : Boolean{
        if(ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            return true
        }
        return false
    }

    private fun requestPermission(){
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION),Constants.PERMISSION_REQUEST_ACCESS_LOCATION)
    }

    override fun onLocationChanged(location: Location) {
        val chip = TinyDB(this).getString(Constants.ID_CHIP)
        val locationSend = com.electric.ccapy.Models.Location(location.latitude,
            location.longitude)
        if(chip != ""){
            DeviceDataProvider().setLocationDevice(this,locationSend)
        }
    }
    override fun onProviderDisabled(provider: String) {}
    override fun onProviderEnabled(provider: String) {}
    @Deprecated("Deprecated in Java")
    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

    override fun onResume() {
        super.onResume()
        getLocation()
    }

}
