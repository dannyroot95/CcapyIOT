package com.electric.ccapy.UI

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.electric.ccapy.Models.DataDevice
import com.electric.ccapy.Models.Users
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

    }
}