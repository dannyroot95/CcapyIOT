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

        InvoiceProvider().getInvoice(this,binding)

    }
}