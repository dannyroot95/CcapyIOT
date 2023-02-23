package com.electric.ccapy.Providers

import android.annotation.SuppressLint
import android.view.View
import android.widget.Toast
import com.electric.ccapy.Models.Config
import com.electric.ccapy.Models.DataDevice
import com.electric.ccapy.Models.Metrics
import com.electric.ccapy.UI.MetricsActivity
import com.electric.ccapy.Utils.Constants
import com.electric.ccapy.databinding.ActivityMetricsBinding
import com.google.firebase.firestore.FirebaseFirestore

class MetricsProvider {

    var db : FirebaseFirestore = FirebaseFirestore.getInstance()

    @SuppressLint("SetTextI18n")
    fun getMetrics(activity: MetricsActivity, binding: ActivityMetricsBinding){
        db.collection(Constants.METRICS).document(Constants.DATA).get().addOnSuccessListener {snapshot ->
            if(snapshot.exists()){
                val metrics = snapshot.toObject(Metrics::class.java)!!
                binding.priceKwh.text = "S/"+metrics.price_kwh
                binding.alicuota.text = "S/"+metrics.alicuota
                binding.interes.text = "S/"+metrics.interes
                binding.noResidential.text = "S/"+metrics.price_no_residential
                binding.txtChrNoResidential.text = "S/"+metrics.price_charge_no_residential
                binding.residential.text = "S/"+metrics.price_residential_30
                binding.txtChrResidential30.text = "S/"+metrics.price_charge_residential_30
                binding.residential31.text = "S/"+metrics.price_residential_31
                binding.txtChrResidential31.text = "S/"+metrics.price_charge_residential_31
                binding.residential140.text = "S/"+metrics.price_residential_140
                binding.txtChrResidential140.text = "S/"+metrics.price_charge_residential_140
                binding.reconecct.text = "S/"+metrics.reconecct
                binding.law.text = "S/"+metrics.law_28749
                binding.progress.visibility = View.GONE
                binding.lnShow.visibility = View.VISIBLE
            }else{
                activity.finish()
                Toast.makeText(activity,"Sin datos",Toast.LENGTH_SHORT).show()
            }
        }
    }
}