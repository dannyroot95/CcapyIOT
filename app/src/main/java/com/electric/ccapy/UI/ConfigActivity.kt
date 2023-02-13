package com.electric.ccapy.UI

import com.electric.ccapy.R
import android.annotation.SuppressLint
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.DatePicker.OnDateChangedListener
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.electric.ccapy.Models.Config
import com.electric.ccapy.Providers.DeviceDataProvider
import com.electric.ccapy.Utils.Constants
import com.electric.ccapy.Utils.TinyDB
import com.electric.ccapy.databinding.ActivityConfigBinding


class ConfigActivity : AppCompatActivity() {

    private lateinit var binding : ActivityConfigBinding
    var dayInvoice : String = ""
    var typeHome : String = ""

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfigBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()

        binding.btnSave.setOnClickListener {
            saveConfig()
        }
        binding.datePicker.setOnDateChangedListener { datePicker, _, _, _ ->
            dayInvoice = datePicker.dayOfMonth.toString()+"/"+(datePicker.month+1).toString()+"/"+datePicker.year.toString()
        }

    }

    private fun saveConfig(){

        val actualRead = binding.edtActualRead.text.toString()
        val actualMedRead = binding.edtActualMed.text.toString()

        if(actualMedRead != "" && actualRead != "" && dayInvoice != "" && typeHome != "" && typeHome != "Seleccione una opción..."){

            if(actualMedRead.toFloat() > actualRead.toFloat()){
                val db = TinyDB(this)
                val config = Config(actualRead,actualMedRead,"",dayInvoice,false,false,"false",0,typeHome)
                db.putString(Constants.KEY_CONFIG_DATA,Constants.STRING_CONFIG_DATA)
                db.putObject(Constants.CONFIG,config)
                DeviceDataProvider().setConfigData(config,this)
            }else{
                Toast.makeText(this,"La lectura del medidor debe ser mayor a la lectura de la factura!",Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(this,"Complete los campos!",Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupUI(){
        initMonthPicker()
        val cConfig = TinyDB(this).getString(Constants.KEY_CONFIG_DATA)
        if (cConfig != ""){
            val config = TinyDB(this).getObject(Constants.CONFIG,Config::class.java)!!
            val value : Int
            binding.edtActualRead.setText(config.actual_read)
            binding.edtActualMed.setText(config.actual_med_read)

            value = if(config.type_home == "Residencial"){ 1 }else{ 2 }

            val adapterSpinner =
                ArrayAdapter.createFromResource(this,  R.array.type_home,
                    R.layout.spinner)
            adapterSpinner.setDropDownViewResource(R.layout.spinner)
            binding.spinner.adapter = adapterSpinner
            binding.spinner.setSelection(value)
            typeHome = config.type_home
            binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    val type = p0!!.getItemAtPosition(p2).toString()
                    typeHome = type
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }

        }else{
            val adapterSpinner =
                ArrayAdapter.createFromResource(this,  R.array.type_home,
                    R.layout.spinner)
            adapterSpinner.setDropDownViewResource(R.layout.spinner)
            binding.spinner.adapter = adapterSpinner
            binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    val type = p0!!.getItemAtPosition(p2).toString()
                    typeHome = type
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }
        }

    }

    @SuppressLint("DiscouragedApi")
    fun initMonthPicker() {
        val dp_mes = binding.datePicker as DatePicker
        val year: Int = dp_mes.year
        val month: Int = dp_mes.month
        var day: Int = 0

        val cConfig = TinyDB(this).getString(Constants.KEY_CONFIG_DATA)
        day = if (cConfig != ""){
            val config = TinyDB(this).getObject(Constants.CONFIG,Config::class.java)!!
            dayInvoice = config.day_invoice
            config.day_invoice.split("/")[0].toInt()

        }else{
            dp_mes.dayOfMonth
        }

        dp_mes.init(year, month, day,
            OnDateChangedListener { view, year, monthOfYear, dayOfMonth ->
                val month_i = monthOfYear + 1
                Log.e("selected month:", month_i.toString())
                //Add whatever you need to handle Date changes
            })
        val daySpinnerId: Int = Resources.getSystem().getIdentifier("day", "id", "android")
        if (daySpinnerId != 0) {
            val daySpinner: View = dp_mes.findViewById(daySpinnerId)
            daySpinner.visibility = View.VISIBLE
        }
        val monthSpinnerId: Int = Resources.getSystem().getIdentifier("month", "id", "android")
        if (monthSpinnerId != 0) {
            val monthSpinner: View = dp_mes.findViewById(monthSpinnerId)
            monthSpinner.visibility = View.VISIBLE
        }
        val yearSpinnerId: Int = Resources.getSystem().getIdentifier("year", "id", "android")
        if (yearSpinnerId != 0) {
            val yearSpinner: View = dp_mes.findViewById(yearSpinnerId)
            yearSpinner.visibility = View.GONE
        }
    }

}