package com.electric.ccapy.Providers

import android.annotation.SuppressLint
import android.view.View
import com.electric.ccapy.Models.Config
import com.electric.ccapy.Models.DataDevice
import com.electric.ccapy.Models.Metrics
import com.electric.ccapy.Models.Users
import com.electric.ccapy.UI.InvoiceActivity
import com.electric.ccapy.Utils.Constants
import com.electric.ccapy.Utils.Convert
import com.electric.ccapy.Utils.TinyDB
import com.electric.ccapy.databinding.ActivityInvoiceBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class InvoiceProvider {

    var dr : DatabaseReference = Firebase.database.reference
    var db : FirebaseFirestore = FirebaseFirestore.getInstance()
    @SuppressLint("SetTextI18n")
    fun getInvoice(activity: InvoiceActivity, binding: ActivityInvoiceBinding){

        val cache = TinyDB(activity)
        val idChip = cache.getString(Constants.ID_CHIP).replace("\"","")
        val user = cache.getObject(Constants.USER, Users::class.java)!!

        dr.child(Constants.DEVICES).child(idChip).child(Constants.CURRENT_DATA).addListenerForSingleValueEvent(object:ValueEventListener{
            @SuppressLint("SimpleDateFormat")
            override fun onDataChange(snapshotDevice: DataSnapshot) {
                if (snapshotDevice.exists()){
                    val device = snapshotDevice.getValue(DataDevice::class.java)!!
                    db.collection(Constants.CONFIG).document(AuthProviders().getCurrentUserID()).get().addOnSuccessListener { snapshot->
                        if (snapshot.exists()){
                            val config = snapshot.toObject(Config::class.java)
                            db.collection(Constants.METRICS).document(Constants.DATA).get().addOnSuccessListener { snapshotM->
                                if (snapshotM.exists()){
                                    val metrics = snapshotM.toObject(Metrics::class.java)!!

                                    val sdfDate = SimpleDateFormat("dd/MM/yyyy hh:mm")
                                    val netDate = Date(System.currentTimeMillis())
                                    val myDate = sdfDate.format(netDate)

                                    binding.txtDate.text = myDate

                                    binding.ivName.text = user.fullname
                                    if(user.address == ""){
                                        binding.ivAddress.text = "SIN DIRECCIÓN REGISTRADA"
                                    }else{
                                        binding.ivAddress.text = user.address
                                    }
                                    //---------------------------------------------------------------
                                    binding.ivTypeHome.text = "BT5-B "+ config!!.type_home.uppercase(
                                        Locale.ROOT
                                    )
                                    //---------------------------------------------------------------
                                    binding.ivActualMed.text = config.actual_med_read+" kw/h"
                                    binding.ivSensorMed.text = device.energy.toString()
                                    binding.ivFactor.text = device.power_factor.toString()

                                    var energy = 0F
                                    val configCache = cache.getObject(Constants.CONFIG,Config::class.java)!!
                                    energy = if(configCache.actual_read != "0.0"){
                                        configCache.actual_med_read.toFloat()-configCache.actual_read.toFloat()+device.energy
                                    }else{
                                        device.energy
                                    }

                                    var charge = ""
                                    if(config.type_home == "Residencial"){
                                        if(energy <= 30.0){
                                            binding.ivPriceKwh.text = metrics.price_residential_30
                                            binding.ivCf.text = metrics.price_charge_residential_30
                                            charge = metrics.price_charge_residential_30
                                        }else if(energy > 30.0 && energy < 140){
                                            binding.ivPriceKwh.text = metrics.price_residential_31
                                            binding.ivCf.text = metrics.price_charge_residential_31
                                            charge = metrics.price_charge_residential_31
                                        }else{
                                            binding.ivPriceKwh.text = metrics.price_residential_140
                                            binding.ivCf.text = metrics.price_charge_residential_140
                                            charge = metrics.price_charge_residential_140
                                        }
                                    }else{
                                        binding.ivPriceKwh.text = metrics.price_kwh
                                        binding.ivCf.text = metrics.price_charge_no_residential
                                        charge = metrics.price_charge_no_residential
                                    }
                                    //---------------------------------------------------------------
                                    val energyCost = Convert().twoDecimals((energy*metrics.price_kwh.toFloat()))
                                    val alicuota = Convert().twoDecimals(metrics.alicuota.toFloat()*7)
                                    binding.ivAp.text = alicuota
                                    binding.ivEnergyCost.text = energyCost
                                    binding.ivItCp.text = metrics.interes
                                    binding.ivMantRe.text = metrics.reconecct
                                    //---------------------------------------------------------------
                                    val subtotal = Convert().twoDecimals((charge.toFloat()+energyCost.toFloat()+alicuota.toFloat()
                                            +metrics.interes.toFloat()+metrics.reconecct.toFloat()))
                                    val igv = Convert().twoDecimals((subtotal.toFloat()/100f)*18)
                                    binding.ivSubTotal.text = subtotal
                                    binding.ivIgv.text = igv
                                    //---------------------------------------------------------------
                                    binding.ivLaw.text = metrics.law_28749
                                    //---------------------------------------------------------------
                                    binding.ivTotal.text = "***"+Convert().twoDecimals(subtotal.toFloat()+igv.toFloat()+
                                            metrics.law_28749.toFloat())
                                    binding.lnData.visibility = View.GONE
                                    binding.cvInvoice.visibility = View.VISIBLE
                                    //---------------------------------------------------------------
                                }else{
                                    //DATOS INCOMPLETOS
                                }
                            }
                        }else{
                            //DATOS INCOMPLETOS
                        }
                    }
                }else{
                    //DATOS INCOMPLETOS
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })


    }

}