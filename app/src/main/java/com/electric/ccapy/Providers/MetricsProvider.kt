package com.electric.ccapy.Providers

import android.annotation.SuppressLint
import android.view.View
import android.widget.Toast
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
                binding.priceKwh.text = "S/"+snapshot["price_kwh"].toString()
                binding.alicuota.text = "S/"+snapshot["alicuota"].toString()
                binding.interes.text = "S/"+snapshot["interes"].toString()
                binding.noResidential.text = "S/"+snapshot["price_no_residential"].toString()
                binding.residential.text = "S/"+snapshot["price_residential"].toString()
                binding.reconecct.text = "S/"+snapshot["reconecct"].toString()
                binding.progress.visibility = View.GONE
                binding.lnShow.visibility = View.VISIBLE
            }else{
                activity.finish()
                Toast.makeText(activity,"Sin datos",Toast.LENGTH_SHORT).show()
            }
        }
    }
}