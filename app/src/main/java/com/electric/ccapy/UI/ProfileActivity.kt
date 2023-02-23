package com.electric.ccapy.UI

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.electric.ccapy.Models.DataDevice
import com.electric.ccapy.Models.Users
import com.electric.ccapy.Providers.AuthProviders
import com.electric.ccapy.Utils.Constants
import com.electric.ccapy.Utils.TinyDB
import com.electric.ccapy.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding : ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = TinyDB(this).getObject(Constants.USER, Users::class.java)!!
        binding.edtFullname.setText(db.fullname)
        binding.edtDni.setText(db.dni)
        binding.edtPhone.setText(db.phone)
        binding.edtEmail.setText(db.email)

        if(db.address != ""){
            binding.edtAddress.setText(db.address)
        }

        binding.btnUpdateData.setOnClickListener {
            val phone = binding.edtPhone.text.toString()
            val address = binding.edtAddress.text.toString()

            if(phone != "" && address != ""){
                AuthProviders().update(phone,address,this)
            }else{
                Toast.makeText(this,"Complete los campos!!",Toast.LENGTH_SHORT).show()
            }

        }

    }
}