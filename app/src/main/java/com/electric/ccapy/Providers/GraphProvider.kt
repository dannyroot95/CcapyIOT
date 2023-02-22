package com.electric.ccapy.Providers

import android.app.Activity
import android.graphics.Color
import android.view.View
import com.electric.ccapy.Models.DataDevice
import com.electric.ccapy.Utils.Constants
import com.electric.ccapy.Utils.TinyDB
import com.electric.ccapy.databinding.ActivityCurrentGraphicsBinding
import com.electric.ccapy.databinding.ActivityPowerGraphBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries

class GraphProvider {

    var db : DatabaseReference = Firebase.database.reference

    fun getValues(activity : Activity , binding: ActivityPowerGraphBinding){
        val series : LineGraphSeries<DataPoint> = LineGraphSeries()
        val g : GraphView = binding.graph
        g.addSeries(series)
        g.title = "Variación de potencia"

        g.viewport.isYAxisBoundsManual = true
        g.viewport.setMinY(0.0)
        g.viewport.setMaxY(150.0)
        g.viewport.isScrollable
        g.viewport.setScrollableY(true)
        val idChip = TinyDB(activity).getString(Constants.ID_CHIP).replace("\"","")
        db.child(Constants.DEVICES).child(idChip).child(Constants.REGISTERS).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    val dp = arrayOfNulls<DataPoint>(
                        snapshot.childrenCount.toInt()
                    )
                    for((index, postSnapshot) in snapshot.children.withIndex()){
                        val dataX = postSnapshot.getValue(DataDevice::class.java)!!
                        dp[index] = DataPoint(index.toDouble(),dataX.watts.toInt().toDouble())
                    }
                    series.resetData(dp)
                    binding.lnLoader.visibility = View.GONE
                    binding.graph.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    fun getValuesCurrent(activity : Activity , binding: ActivityCurrentGraphicsBinding){
        val series : LineGraphSeries<DataPoint> = LineGraphSeries()
        val g : GraphView = binding.graph
        g.addSeries(series)
        g.title = "Variación de corriente"

        g.viewport.isYAxisBoundsManual = true
        g.viewport.setMinY(0.0)
        g.viewport.setMaxY(150.0)
        g.viewport.isScrollable
        g.viewport.borderColor = Color.GREEN
        g.viewport.setScrollableY(true)
        val idChip = TinyDB(activity).getString(Constants.ID_CHIP).replace("\"","")
        db.child(Constants.DEVICES).child(idChip).child(Constants.REGISTERS).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    val dp = arrayOfNulls<DataPoint>(
                        snapshot.childrenCount.toInt()
                    )
                    for((index, postSnapshot) in snapshot.children.withIndex()){
                        val dataX = postSnapshot.getValue(DataDevice::class.java)!!
                        dp[index] = DataPoint(index.toDouble(),dataX.ampere.toInt().toDouble())
                    }
                    series.resetData(dp)
                    binding.lnLoader.visibility = View.GONE
                    binding.graph.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

}