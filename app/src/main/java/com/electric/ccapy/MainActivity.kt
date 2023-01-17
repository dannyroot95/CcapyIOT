package com.electric.ccapy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.electric.ccapy.Providers.AuthProviders
import com.electric.ccapy.UI.RegisterActivity
import com.electric.ccapy.databinding.ActivityMainBinding

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
}