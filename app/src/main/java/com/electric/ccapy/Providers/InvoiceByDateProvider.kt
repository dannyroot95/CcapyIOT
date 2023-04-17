package com.electric.ccapy.Providers

import android.annotation.SuppressLint
import android.app.Activity
import android.view.View
import android.widget.Toast
import com.electric.ccapy.Models.Config
import com.electric.ccapy.Models.DataDevice
import com.electric.ccapy.Models.Metrics
import com.electric.ccapy.Models.Users
import com.electric.ccapy.Utils.Constants
import com.electric.ccapy.Utils.Convert
import com.electric.ccapy.Utils.StringToTimestamp
import com.electric.ccapy.Utils.TinyDB
import com.electric.ccapy.databinding.ActivityInvoiceBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.*

class InvoiceByDateProvider {

    var dr : DatabaseReference = Firebase.database.reference
    var db : FirebaseFirestore = FirebaseFirestore.getInstance()

    fun receiveMonths(value:String,activity: Activity,binding:ActivityInvoiceBinding,m:String,y:String){
        val cache = TinyDB(activity)
        val idChip = cache.getString(Constants.ID_CHIP).replace("\"","")
        val date = value.split(",")
        val initDate = StringToTimestamp().value(date[0])
        val finalDate = StringToTimestamp().value(date[1])
        val user = cache.getObject(Constants.USER, Users::class.java)!!

        dr.child(Constants.DEVICES).child(idChip).child(Constants.REGISTERS).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    val items = arrayListOf(0f)
                    for(postSnapshot in snapshot.children){
                        val data = postSnapshot.getValue(DataDevice::class.java)!!
                        if (data.time in initDate..finalDate){
                            items.add(data.energy)
                        }
                    }

                    if(items.isNotEmpty() && items.size > 1){
                        val max = items.maxOrNull()!!.toFloat()

                        dr.child(Constants.DEVICES).child(idChip).child(Constants.CURRENT_DATA).addListenerForSingleValueEvent(object:ValueEventListener{
                            @SuppressLint("SimpleDateFormat", "SetTextI18n")
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

                                                    binding.ivMonthInvoice.text = m.toUpperCase(
                                                        Locale.ROOT)+" "+y

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
                                                    binding.ivSensorMed.text = "$max kw/h"
                                                    binding.ivFactor.text = device.power_factor.toString()

                                                    val configCache = cache.getObject(Constants.CONFIG,
                                                        Config::class.java)!!

                                                    var charge = ""
                                                    if(config.type_home == "Residencial"){
                                                        if (max <= 30.0) {
                                                            binding.ivPriceKwh.text =
                                                                metrics.price_residential_30
                                                            binding.ivCf.text =
                                                                metrics.price_charge_residential_30
                                                            charge =
                                                                metrics.price_charge_residential_30
                                                        } else if (max > 30.0 && max < 140) {
                                                            binding.ivPriceKwh.text =
                                                                metrics.price_residential_31
                                                            binding.ivCf.text =
                                                                metrics.price_charge_residential_31
                                                            charge =
                                                                metrics.price_charge_residential_31
                                                        } else {
                                                            binding.ivPriceKwh.text =
                                                                metrics.price_residential_140
                                                            binding.ivCf.text =
                                                                metrics.price_charge_residential_140
                                                            charge =
                                                                metrics.price_charge_residential_140
                                                        }
                                                    }else{
                                                        binding.ivPriceKwh.text = metrics.price_kwh
                                                        binding.ivCf.text = metrics.price_charge_no_residential
                                                        charge = metrics.price_charge_no_residential
                                                    }
                                                    //---------------------------------------------------------------
                                                    val energyCost = Convert().twoDecimals((max * metrics.price_kwh.toFloat()))
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
                                                    binding.ivTotal.text = "***"+ Convert().twoDecimals(subtotal.toFloat()+igv.toFloat()+
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



                    }else{
                        Toast.makeText(activity,"Sin Datos!",Toast.LENGTH_SHORT).show()
                        binding.lnData.visibility = View.GONE
                        binding.cvInvoice.visibility = View.VISIBLE
                    }

                }
            }
            override fun onCancelled(p0: DatabaseError) {
            }
        })

    }

}