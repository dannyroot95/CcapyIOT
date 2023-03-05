package com.electric.ccapy.UI.Graphics

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.electric.ccapy.Models.Config
import com.electric.ccapy.Models.DataDevice
import com.electric.ccapy.Utils.Constants
import com.electric.ccapy.Utils.StringToTimestamp
import com.electric.ccapy.Utils.TinyDB
import com.electric.ccapy.databinding.ActivityEnergyGraphBinding
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList


class EnergyGraph : AppCompatActivity() {

    private lateinit var binding : ActivityEnergyGraphBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEnergyGraphBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db : DatabaseReference = Firebase.database.reference
        val ty = TinyDB(this)
        val strConfig = ty.getString(Constants.KEY_CONFIG_DATA)

        if(strConfig != ""){

            val idChip = ty.getString(Constants.ID_CHIP).replace("\"","")
            val config = ty.getObject(Constants.CONFIG,Config::class.java)!!
            val day = config.day_invoice.split("/")[0]

            val m1 = arrayOf(
                StringToTimestamp().value("01/01/2023 00:01"),StringToTimestamp().value("01/02/2023 00:01"),
                StringToTimestamp().value("01/03/2023 00:01"),StringToTimestamp().value("01/04/2023 00:01"),
                StringToTimestamp().value("01/05/2023 00:01"),StringToTimestamp().value("01/06/2023 00:01"),
                StringToTimestamp().value("01/07/2023 00:01"),StringToTimestamp().value("01/08/2023 00:01"),
                StringToTimestamp().value("01/09/2023 00:01"),StringToTimestamp().value("01/10/2023 00:01"),
                StringToTimestamp().value("01/11/2023 00:01"),StringToTimestamp().value("01/12/2023 00:01"),
            )

            val m2 = arrayOf(
                StringToTimestamp().value("$day/01/2023 23:59"),StringToTimestamp().value("$day/02/2023 23:59"),
                StringToTimestamp().value("$day/03/2023 23:59"),StringToTimestamp().value("$day/04/2023 23:59"),
                StringToTimestamp().value("$day/05/2023 23:59"),StringToTimestamp().value("$day/06/2023 23:59"),
                StringToTimestamp().value("$day/07/2023 23:59"),StringToTimestamp().value("$day/08/2023 23:59"),
                StringToTimestamp().value("$day/09/2023 23:59"),StringToTimestamp().value("$day/10/2023 23:59"),
                StringToTimestamp().value("$day/11/2023 23:59"),StringToTimestamp().value("$day/12/2023 23:59"),
            )



            db.child(Constants.DEVICES).child(idChip).child(Constants.REGISTERS).addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){

                        val eneArr : ArrayList<Float> = ArrayList()
                        val febArr : ArrayList<Float> = ArrayList()
                        val marArr : ArrayList<Float> = ArrayList()
                        val abrArr : ArrayList<Float> = ArrayList()
                        val mayArr : ArrayList<Float> = ArrayList()
                        val junArr : ArrayList<Float> = ArrayList()
                        val julArr : ArrayList<Float> = ArrayList()
                        val agoArr : ArrayList<Float> = ArrayList()
                        val sepArr : ArrayList<Float> = ArrayList()
                        val octArr : ArrayList<Float> = ArrayList()
                        val novArr : ArrayList<Float> = ArrayList()
                        val dicArr : ArrayList<Float> = ArrayList()


                        for (postSnapshot in snapshot.children){
                            val dataX = postSnapshot.getValue(DataDevice::class.java)!!
                            if(dataX.time > m1[0] && dataX.time < m2[0]){
                                eneArr.add(dataX.energy)
                            } else if(dataX.time > m1[1] && dataX.time < m2[1]){
                                febArr.add(dataX.energy)
                            } else if(dataX.time > m1[2] && dataX.time < m2[2]){
                                marArr.add(dataX.energy)
                            } else if(dataX.time > m1[3] && dataX.time < m2[3]){
                                abrArr.add(dataX.energy)
                            } else if(dataX.time > m1[4] && dataX.time < m2[4]){
                                mayArr.add(dataX.energy)
                            } else if(dataX.time > m1[5] && dataX.time < m2[5]){
                                junArr.add(dataX.energy)
                            } else if(dataX.time > m1[6] && dataX.time < m2[6]){
                                julArr.add(dataX.energy)
                            } else if(dataX.time > m1[7] && dataX.time < m2[7]){
                                agoArr.add(dataX.energy)
                            } else if(dataX.time > m1[8] && dataX.time < m2[8]){
                                sepArr.add(dataX.energy)
                            } else if(dataX.time > m1[9] && dataX.time < m2[9]){
                                octArr.add(dataX.energy)
                            } else if(dataX.time > m1[10] && dataX.time < m2[10]){
                                novArr.add(dataX.energy)
                            } else if(dataX.time > m1[11] && dataX.time < m2[11]){
                                dicArr.add(dataX.energy)
                            }
                        }

                        val v : ArrayList<Float> = ArrayList()
                        var kwh = 0.0f
                        val actualMonth = (Calendar.getInstance().get(Calendar.MONTH)+1).toInt()
                        if(config.actual_read.toFloat() != 0.0f){
                            kwh = config.actual_med_read.toFloat()-config.actual_read.toFloat()
                        }
                        val labels = arrayListOf(
                            "Ene", "Feb", "Mar",
                            "Abr", "May", "Jun",
                            "Jul", "Ago", "Set",
                            "Oct", "Nov", "Dic"
                        )

                        if(eneArr.isEmpty()){
                            v.add(0,0f)
                        }else{
                            if(actualMonth == 1){
                                v.add(0,getMax(eneArr)+kwh)
                            }else{
                                v.add(0,getMax(eneArr))
                            }
                        }
                        if(febArr.isEmpty()){
                            v.add(1,0f)
                        }else{
                            if(actualMonth == 2){
                            v.add(1,getMax(febArr)+kwh)
                            }else{
                                v.add(1,getMax(febArr))
                            }
                        }
                        if(marArr.isEmpty()){
                            v.add(2,0f)
                        }else{
                            if(actualMonth == 3){
                                v.add(2,getMax(marArr)+kwh)
                            }else{
                                v.add(2,getMax(marArr))
                            }
                        }
                        if(abrArr.isEmpty()){
                            v.add(3,0f)
                        }else{
                            if(actualMonth == 4){
                                v.add(3,getMax(abrArr)+kwh)
                            }else{
                                v.add(3,getMax(abrArr))
                            }
                        }
                        if(mayArr.isEmpty()){
                            v.add(4,0f)
                        }else{
                            if(actualMonth == 5){
                                v.add(4,getMax(mayArr)+kwh)
                            }else{
                                v.add(4,getMax(mayArr))
                            }
                        }
                        if(junArr.isEmpty()){
                            v.add(5,0f)
                        }else{
                            if(actualMonth == 6){
                                v.add(5,getMax(junArr)+kwh)
                            }else{
                                v.add(5,getMax(junArr))
                            }
                        }
                        if(julArr.isEmpty()){
                            v.add(6,0f)
                        }else{
                            if(actualMonth == 7){
                                v.add(6,getMax(julArr)+kwh)
                            }else{
                                v.add(6,getMax(julArr))
                            }
                        }
                        if(agoArr.isEmpty()){
                            v.add(7,0f)
                        }else{
                            if(actualMonth == 8){
                                v.add(7,getMax(agoArr)+kwh)
                            }else{
                                v.add(7,getMax(agoArr))
                            }
                        }
                        if(sepArr.isEmpty()){
                            v.add(8,0f)
                        }else{
                            if(actualMonth == 9){
                                v.add(8,getMax(sepArr)+kwh)
                            }else{
                                v.add(8,getMax(sepArr))
                            }
                        }
                        if(octArr.isEmpty()){
                            v.add(9,0f)
                        }else{
                            v.add(9,getMax(octArr))
                        }
                        if(novArr.isEmpty()){
                            v.add(10,0f)
                        }else{
                            v.add(10,getMax(novArr))
                        }
                        if(dicArr.isEmpty()){
                            v.add(11,0f)
                        }else{
                            v.add(11,getMax(dicArr))
                        }

                        val entries = arrayListOf(
                            BarEntry(0f, v[0]),
                            BarEntry(1f, v[1]),
                            BarEntry(2f, v[2]),
                            BarEntry(3f, v[3]),
                            BarEntry(4f, v[4]),
                            BarEntry(5f, v[5]),
                            BarEntry(6f, v[6]),
                            BarEntry(7f, v[7]),
                            BarEntry(8f, v[8]),
                            BarEntry(9f, v[9]),
                            BarEntry(10f, v[10]),
                            BarEntry(11f, v[11])
                        )

                        val set = BarDataSet(entries, "Consumo kw/h - "+ Calendar.getInstance().get(Calendar.YEAR))
                        set.valueTextSize = 12f
                        set.colors = ColorTemplate.createColors(ColorTemplate.MATERIAL_COLORS)

                        binding.graph.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
                        binding.graph.xAxis.position = XAxis.XAxisPosition.TOP

                        binding.graph.setDrawGridBackground(false)
                        binding.graph.axisLeft.isEnabled = false
                        binding.graph.axisRight.isEnabled = false
                        binding.graph.description.isEnabled = false

                        binding.graph.data = BarData(set)
                        binding.graph.invalidate()
                        binding.graph.setFitBars(true)
                        binding.lnLoader.visibility = View.GONE

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

        }else{
            Toast.makeText(this,"Configure su fecha de recibo!!",Toast.LENGTH_SHORT).show()
            finish()
        }

    }

    fun getMax(A: ArrayList<Float>): Float {
        var max = Float.MIN_VALUE
        for (i in A) {
            max = max.coerceAtLeast(i)
        }
        return max
    }

}