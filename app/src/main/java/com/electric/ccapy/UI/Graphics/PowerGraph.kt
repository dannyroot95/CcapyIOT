package com.electric.ccapy.UI.Graphics

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.electric.ccapy.Providers.GraphProvider
import com.electric.ccapy.databinding.ActivityPowerGraphBinding

class PowerGraph : AppCompatActivity() {

    private lateinit var binding : ActivityPowerGraphBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPowerGraphBinding.inflate(layoutInflater)
        setContentView(binding.root)
        GraphProvider().getValues(this,binding)
    }
}