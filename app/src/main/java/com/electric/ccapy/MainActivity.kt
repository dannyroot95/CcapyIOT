package com.electric.ccapy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.electric.ccapy.Models.Users
import com.electric.ccapy.Providers.AuthProviders
import com.electric.ccapy.UI.MenuActivity
import com.electric.ccapy.UI.RegisterActivity
import com.electric.ccapy.UI.SynchronizeDeviceActivity
import com.electric.ccapy.Utils.Constants
import com.electric.ccapy.Utils.TinyDB
import com.electric.ccapy.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {

            val email = binding.edtCode.text.toString()
            val password = binding.edtPassword.text.toString()

            if(email != "" && password != ""){
                AuthProviders().login(email,password,this,binding)
            }else{
                Toast.makeText(this,"Complete los campos!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnRegister.setOnClickListener {
            startActivity(Intent(this,RegisterActivity::class.java))
        }

    }

    override fun onStart() {
        val currentUser  = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val db = TinyDB(this).getObject(Constants.USER, Users::class.java)
            if(db.hasDevice){
                startActivity(Intent(this, MenuActivity::class.java))
                finish()
            }else{
                startActivity(Intent(this, SynchronizeDeviceActivity::class.java))
                finish()
            }
        }
        super.onStart()
    }

    override fun onBackPressed() {
    }

}