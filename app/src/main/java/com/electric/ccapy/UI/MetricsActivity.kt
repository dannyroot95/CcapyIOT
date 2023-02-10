package com.electric.ccapy.UI

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.electric.ccapy.Providers.MetricsProvider
import com.electric.ccapy.databinding.ActivityMetricsBinding

class MetricsActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMetricsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMetricsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MetricsProvider().getMetrics(this,binding)

    }
}