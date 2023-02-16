package com.electric.ccapy.UI

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.electric.ccapy.Adapters.RegistersAdapter
import com.electric.ccapy.Models.DataDevice
import com.electric.ccapy.Providers.RegistersProvider
import com.electric.ccapy.Utils.Constants
import com.electric.ccapy.Utils.TinyDB
import com.electric.ccapy.databinding.ActivityRegistersBinding

class RegistersActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRegistersBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getData()
    }

    fun receiveData(list: ArrayList<DataDevice>){
        if (list.size > 0){
            val db = TinyDB(this)
            binding.progressCircular.visibility = View.GONE
            binding.lnTitle.visibility = View.VISIBLE
            binding.recyclerRegisters.visibility = View.VISIBLE
            binding.recyclerRegisters.layoutManager = LinearLayoutManager(this)
            binding.recyclerRegisters.setHasFixedSize(true)
            val adapter = RegistersAdapter(this, list)
            binding.recyclerRegisters.adapter = adapter
            db.putListObjectRegistersFromDevice(Constants.REGISTERS,list)
            db.putString(Constants.KEY_REGISTERS,Constants.STRING_REGISTERS)
        }else{
            binding.progressCircular.visibility = View.GONE
            Toast.makeText(this,"No hay datos!", Toast.LENGTH_LONG).show()
        }
    }

    private fun getData(){
        val db = TinyDB(this)
        val cacheString = db.getString(Constants.KEY_REGISTERS)
        if(cacheString != ""){
            val list = TinyDB(this).getListObjectRegistersFromDevice(Constants.REGISTERS,DataDevice::class.java)
            binding.progressCircular.visibility = View.GONE
            binding.lnTitle.visibility = View.VISIBLE
            binding.recyclerRegisters.visibility = View.VISIBLE
            binding.recyclerRegisters.layoutManager = LinearLayoutManager(this)
            binding.recyclerRegisters.setHasFixedSize(true)
            val adapter = RegistersAdapter(this, list)
            binding.recyclerRegisters.adapter = adapter
            RegistersProvider().getData(this,binding)
        }else{
            RegistersProvider().getData(this,binding)
        }
    }

}