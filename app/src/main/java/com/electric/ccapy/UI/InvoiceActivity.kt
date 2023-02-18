package com.electric.ccapy.UI

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.electric.ccapy.Models.Users
import com.electric.ccapy.Providers.InvoiceProvider
import com.electric.ccapy.Utils.Constants
import com.electric.ccapy.Utils.TinyDB
import com.electric.ccapy.databinding.ActivityInvoiceBinding

class InvoiceActivity : AppCompatActivity() {

    private lateinit var binding : ActivityInvoiceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInvoiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = TinyDB(this).getObject(Constants.USER, Users::class.java)!!
        binding.ivName.text = user.fullname
        if(user.address == ""){
            binding.ivAddress.text = "SIN DIRECCIÓN REGISTRADA"
        }else{
            binding.ivAddress.text = user.address
        }
        InvoiceProvider().getInvoice(this,binding)

    }
}