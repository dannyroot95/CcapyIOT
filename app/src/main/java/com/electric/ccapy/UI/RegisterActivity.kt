package com.electric.ccapy.UI

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import com.electric.ccapy.Models.Users
import com.electric.ccapy.Providers.AuthProviders
import com.electric.ccapy.Providers.DNIProvider
import com.electric.ccapy.R
import com.electric.ccapy.Utils.Constants
import com.electric.ccapy.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener {

            val dni = binding.edtDni.text.toString()
            val fullname = binding.edtFullname.text.toString()
            val phone = binding.edtPhone.text.toString()
            val email = binding.edtEmail.text.toString()
            val password = binding.edtPassword.text.toString()

            if(dni != "" && fullname != "" && phone != "" && email != "" && password != ""){
                if(password.length >= 6){
                    val user = Users(fullname,email,phone,dni,Constants.STATUS_SUCCESS,Constants.CLIENT)
                    AuthProviders().register(user,password,this,binding)
                }else{
                    Toast.makeText(this,"La contraseña debe tener más de 5 caracteres!", Toast.LENGTH_SHORT).show()
                }

            }else{
                Toast.makeText(this,"Complete los campos!", Toast.LENGTH_SHORT).show()
            }

        }

        binding.edtDni.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0!!.length == 8) {
                    val DNI: String = binding.edtDni.text.toString()
                    DNIProvider().searchDNI(DNI,this@RegisterActivity,binding)
                }else{
                    binding.edtFullname.setText("")
                }
            }

        })

        binding.btnAccess.setOnClickListener {
            finish()
        }

    }

    private fun isExit(){

        val positiveButtonClick = { _: DialogInterface, _: Int ->

            if (binding.btnRegister.isVisible){
                finish()
            }else{
                Toast.makeText(this,"Error : No puede salir ahora!", Toast.LENGTH_SHORT).show()
            }

        }
        val negativeButtonClick = { _: DialogInterface, _: Int ->
        }

        val builder = AlertDialog.Builder(this)

        with(builder)
        {
            setTitle("Alerta!")
            setIcon(R.drawable.ic_user)
            setMessage("Está seguro de salir?")
            setPositiveButton("SI", DialogInterface.OnClickListener(function = positiveButtonClick))
            setNegativeButton("NO", negativeButtonClick)
            show()
        }

    }

    override fun onBackPressed() {
        isExit()
    }

}