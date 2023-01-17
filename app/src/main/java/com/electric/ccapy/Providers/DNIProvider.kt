package com.electric.ccapy.Providers

import android.view.View
import android.widget.Toast
import com.electric.ccapy.Models.DNI
import com.electric.ccapy.Network.InterfaceDNI
import com.electric.ccapy.UI.RegisterActivity
import com.electric.ccapy.Utils.Constants
import com.electric.ccapy.databinding.ActivityRegisterBinding
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DNIProvider {

    fun searchDNI(dni : String, activity : RegisterActivity , binding : ActivityRegisterBinding){
        Toast.makeText(activity.applicationContext, "BUSCANDO DNI , ESPERE....", Toast.LENGTH_SHORT)
            .show()
        binding.edtDni.isEnabled = false
        val gson = GsonBuilder().serializeNulls().create()
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(Constants.URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        val interfaceDNI: InterfaceDNI = retrofit.create(InterfaceDNI::class.java)
        val call: Call<DNI> = interfaceDNI.getDataDni(Constants.BASE_URL+dni)
        call.enqueue(object : Callback<DNI> {
            override fun onResponse(call: Call<DNI>, response: Response<DNI>) {

                if (!response.isSuccessful) {
                    Toast.makeText(activity.applicationContext, "Error !", Toast.LENGTH_SHORT)
                        .show()

                }

                else if (response.body()?.nombre?.isNotBlank()!!) {
                    var dataDNI = ""
                    dataDNI = response.body()!!.nombre
                    binding.edtFullname.setText(dataDNI)
                    binding.edtDni.isEnabled = true
                } else {

                    Toast.makeText(activity.applicationContext, "Error de DNI", Toast.LENGTH_LONG)
                        .show()
                    binding.edtDni.isEnabled = true
                    binding.edtFullname.setText("")
                }
            }

            override fun onFailure(call: Call<DNI>, t: Throwable) {
                Toast.makeText(
                    activity.applicationContext,
                    "Error!, inténtelo mas tarde",
                    Toast.LENGTH_LONG
                ).show()
                binding.edtDni.isEnabled = true
                binding.edtFullname.setText("")
            }
        })
    }

}