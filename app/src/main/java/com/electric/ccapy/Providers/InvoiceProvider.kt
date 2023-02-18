package com.electric.ccapy.Providers

import android.annotation.SuppressLint
import com.electric.ccapy.Models.Config
import com.electric.ccapy.Models.Metrics
import com.electric.ccapy.UI.InvoiceActivity
import com.electric.ccapy.Utils.Constants
import com.electric.ccapy.databinding.ActivityInvoiceBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import java.util.*

class InvoiceProvider {

    var dr : DatabaseReference = Firebase.database.reference
    var db : FirebaseFirestore = FirebaseFirestore.getInstance()

    @SuppressLint("SetTextI18n")
    fun getInvoice(activity: InvoiceActivity, binding: ActivityInvoiceBinding){
        db.collection(Constants.CONFIG).document(AuthProviders().getCurrentUserID()).get().addOnSuccessListener { snapshot->
            if (snapshot.exists()){
                val config = snapshot.toObject(Config::class.java)
                db.collection(Constants.METRICS).document(Constants.DATA).get().addOnSuccessListener { snapshotM->
                    if (snapshotM.exists()){
                        val metrics = snapshotM.toObject(Metrics::class.java)!!
                        binding.ivTypeHome.text = "BT5-B "+config!!.type_home.toUpperCase(Locale.ROOT)
                        binding.ivActualMed.text = config.actual_med_read+" kw/h"
                        val energy = 10.2
                        if(config.type_home == "Residencial"){
                            if(energy <= 30.0){
                                binding.ivPriceKwh.text = metrics.price_residential_30
                            }else if(energy > 30.0 && energy < 140){
                                binding.ivPriceKwh.text = metrics.price_residential_31
                            }else{
                                binding.ivPriceKwh.text = metrics.price_residential_140
                            }
                        }else{
                            binding.ivPriceKwh.text = metrics.price_kwh
                        }
                    }
                }

            }
        }
    }

}