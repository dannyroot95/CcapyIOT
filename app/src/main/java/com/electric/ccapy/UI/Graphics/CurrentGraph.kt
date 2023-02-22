package com.electric.ccapy.UI.Graphics

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.electric.ccapy.Providers.GraphProvider
import com.electric.ccapy.databinding.ActivityCurrentGraphicsBinding
import com.electric.ccapy.databinding.ActivityPowerGraphBinding

class CurrentGraph : AppCompatActivity() {

    private lateinit var binding : ActivityCurrentGraphicsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCurrentGraphicsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        GraphProvider().getValuesCurrent(this,binding)
    }
}