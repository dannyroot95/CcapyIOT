package com.electric.ccapy.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.electric.ccapy.Models.DataDevice
import com.electric.ccapy.databinding.ItemRegisterBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


open class RegistersAdapter(private val context: Context,
                          private var list: ArrayList<DataDevice>) : RecyclerView.Adapter<RegistersAdapter.MyViewHolder>()  {

    inner class MyViewHolder(val binding: ItemRegisterBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemRegisterBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model = list[position]
        val sdfDate = SimpleDateFormat("dd/MM/yyyy hh:mm")
        val netDate = Date(model.time*1000)
        val myDate = sdfDate.format(netDate)

        with(holder){
            binding.itVoltage.text = model.voltage.toString()+"V"
            binding.itDate.text = "Fecha y Hora : $myDate"
            binding.itAmpere.text = "Corriente "+"\u2192"+"  "+model.ampere.toString()+"A"
            binding.itPower.text = "Potencia "+"\u2192"+"  "+model.watts.toString()+"W"
            binding.itEnergy.text = "Energía "+"\u2192"+"  "+model.energy.toString()+"kw/h"
            binding.itFrequency.text = "Frecuencia "+"\u2192"+"  "+model.frequency.toString()+"Hz"
            binding.itPf.text = "Factor de potencia "+"\u2192"+"  "+model.power_factor.toString()+"pf"
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

}