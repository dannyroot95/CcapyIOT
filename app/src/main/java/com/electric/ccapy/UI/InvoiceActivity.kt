package com.electric.ccapy.UI

import android.R
import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.electric.ccapy.Models.Users
import com.electric.ccapy.Providers.InvoiceProvider
import com.electric.ccapy.Utils.Constants
import com.electric.ccapy.Utils.RetrieveDateByMonth
import com.electric.ccapy.Utils.TinyDB
import com.electric.ccapy.databinding.ActivityInvoiceBinding
import java.util.*

class InvoiceActivity : AppCompatActivity() {

    private lateinit var binding : ActivityInvoiceBinding
    val months = arrayOf(
        "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
        "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
    )

    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    val years = (currentYear - 50..currentYear).toList().reversed().map { it.toString() }.toTypedArray()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInvoiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        InvoiceProvider().getInvoice(this,binding)

        binding.date.setOnClickListener {

            val view = LayoutInflater.from(this).inflate(com.electric.ccapy.R.layout.dates, null)
            val spinnerMonth = view.findViewById<Spinner>(com.electric.ccapy.R.id.spinnerMonth)
            val spinnerYear = view.findViewById<Spinner>(com.electric.ccapy.R.id.spinnerYear)

            spinnerMonth.adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, months)
            spinnerYear.adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, years)

            AlertDialog.Builder(this)
                .setTitle("Seleccione una fecha")
                .setView(view)
                .setPositiveButton("Aceptar") { dialog, which ->
                    val month = spinnerMonth.selectedItem.toString()
                    val year = spinnerYear.selectedItem.toString()
                    binding.lnData.visibility = View.VISIBLE
                    binding.cvInvoice.visibility = View.GONE
                    RetrieveDateByMonth().getMonth(this,month,binding,year)
                    // Aquí puedes obtener el mes seleccionado
                }
                .setNegativeButton("Cancelar", null)
                .show()

        }

    }



}