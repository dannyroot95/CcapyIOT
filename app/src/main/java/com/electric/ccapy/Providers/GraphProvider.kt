package com.electric.ccapy.Providers

import android.annotation.SuppressLint
import android.app.Activity
import android.view.View
import com.electric.ccapy.Models.DataDevice
import com.electric.ccapy.R
import com.electric.ccapy.Utils.Constants
import com.electric.ccapy.Utils.TinyDB
import com.electric.ccapy.databinding.ActivityCurrentGraphicsBinding
import com.electric.ccapy.databinding.ActivityPowerGraphBinding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class GraphProvider {

    var db : DatabaseReference = Firebase.database.reference

    fun getValues(activity : Activity , binding: ActivityPowerGraphBinding){
        val array : ArrayList<Double> = ArrayList()
        val linevalues = ArrayList<Entry>()
        val idChip = TinyDB(activity).getString(Constants.ID_CHIP).replace("\"","")
        db.child(Constants.DEVICES).child(idChip).child(Constants.REGISTERS).addListenerForSingleValueEvent(object :
            ValueEventListener {
            val arrayDate = ArrayList<String>()
            var ctx = 0
            var c = 0
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(postSnapshot in snapshot.children){
                        val dataX = postSnapshot.getValue(DataDevice::class.java)!!
                        val sdfDate = SimpleDateFormat("dd/MM/yyyy hh:mm")
                        val netDate = Date(dataX.time*1000)
                        val myDate = sdfDate.format(netDate)
                        arrayDate.add(myDate)
                        array.add(ctx,dataX.watts.toInt().toDouble())
                        ctx++
                    }

                    for (i in 0 until array.size){
                        c += 1
                        linevalues.add(Entry(c.toFloat(),array[i].toFloat()))
                    }

                    val linedataset = LineDataSet(linevalues, "Consumo de potencia")
                    //We add features to our chart
                    linedataset.setDrawFilled(true)
                    linedataset.color = activity.resources.getColor(R.color.teal_200)
                    linedataset.setDrawCircles(false)
                    linedataset.fillColor = activity.resources.getColor(R.color.purple_500)
                    linedataset.setDrawFilled(true)
                    linedataset.valueTextSize = 14F
                    linedataset.mode = LineDataSet.Mode.CUBIC_BEZIER

                    //We connect our data to the UI Screen
                    val data = LineData(linedataset)
                    binding.graph.data = data
                    binding.graph.description.isEnabled = false
                    binding.graph.setBackgroundColor(activity.resources.getColor(R.color.white))
                    binding.graph.xAxis.valueFormatter = IndexAxisValueFormatter(arrayDate)
                    binding.graph.xAxis.position = XAxis.XAxisPosition.TOP
                    binding.lnLoader.visibility = View.GONE



                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    fun getValuesCurrent(activity : Activity , binding: ActivityCurrentGraphicsBinding){
        val array : ArrayList<Double> = ArrayList()
        val linevalues = ArrayList<Entry>()
        val idChip = TinyDB(activity).getString(Constants.ID_CHIP).replace("\"","")
        db.child(Constants.DEVICES).child(idChip).child(Constants.REGISTERS).addListenerForSingleValueEvent(object :
            ValueEventListener {
            val arrayDate = ArrayList<String>()
            var ctx = 0
            var c = 0
            @SuppressLint("SimpleDateFormat")
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for((index, postSnapshot) in snapshot.children.withIndex()){
                        val dataX = postSnapshot.getValue(DataDevice::class.java)!!
                        val sdfDate = SimpleDateFormat("dd/MM/yyyy hh:mm")
                        val netDate = Date(dataX.time*1000)
                        val myDate = sdfDate.format(netDate)
                        arrayDate.add(myDate)
                        array.add(ctx,dataX.ampere.toInt().toDouble())
                        ctx++
                    }

                    for (i in 0 until array.size){
                        c += 1
                        linevalues.add(Entry(c.toFloat(),array[i].toFloat()))
                    }

                    val linedataset = LineDataSet(linevalues, "Consumo de corriente")
                    //We add features to our chart
                    linedataset.setDrawFilled(true)
                    linedataset.color = activity.resources.getColor(R.color.teal_700)
                    linedataset.setDrawCircles(false)
                    linedataset.fillColor = activity.resources.getColor(R.color.purple_700)
                    linedataset.setDrawFilled(true)
                    linedataset.valueTextSize = 14F
                    linedataset.mode = LineDataSet.Mode.CUBIC_BEZIER

                    //We connect our data to the UI Screen
                    val data = LineData(linedataset)
                    binding.graph.data = data
                    binding.graph.description.isEnabled = false
                    binding.graph.setBackgroundColor(activity.resources.getColor(R.color.white))
                    binding.graph.xAxis.valueFormatter = IndexAxisValueFormatter(arrayDate)
                    binding.graph.xAxis.position = XAxis.XAxisPosition.TOP
                    binding.lnLoader.visibility = View.GONE

                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

}