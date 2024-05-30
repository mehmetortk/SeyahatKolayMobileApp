package com.example.seyahatkolaymobileapp

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.DatePicker
import android.widget.ProgressBar
import android.widget.Spinner
import androidx.fragment.app.Fragment
import java.util.Calendar

class TicketFragment : Fragment() {
    private var handler: Handler? = null

    private var spinner1: Spinner? = null
    private var spinner2: Spinner? = null
    private var btnDateTimePicker: Button? = null
    private var btnChangeDirection: Button? = null
    private var searchButton: Button? = null
    private var progressBar: ProgressBar? = null
    private val sehirler: Array<String> = arrayOf(
        "Şehir Seçiniz",
        "Adana",
        "Adıyaman",
        "Afyon",
        // Diğer şehirler buraya eklenebilir
        "Düzce"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_ticket, container, false)
        progressBar = view.findViewById(R.id.progressBar)
        btnDateTimePicker = view.findViewById(R.id.btnDateTimePicker)
        btnChangeDirection = view.findViewById(R.id.btnChangeDirection)
        spinner1 = view.findViewById(R.id.spinner1)
        handler = Handler()
        spinner2 = view.findViewById(R.id.spinner2)
        searchButton = view.findViewById(R.id.searchButton)

        val adapter = ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_item, sehirler)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner1?.adapter = adapter
        spinner2?.adapter = adapter

        btnChangeDirection?.setOnClickListener {
            val spinner1Position = spinner1?.selectedItemPosition ?: 0
            val spinner2Position = spinner2?.selectedItemPosition ?: 0

            spinner1?.setSelection(spinner2Position)
            spinner2?.setSelection(spinner1Position)
        }

        btnDateTimePicker?.setOnClickListener { showDateTimePicker() }

        searchButton?.setOnClickListener {
            progressBar?.visibility = View.VISIBLE
            startProgressBar()
        }

        spinner1?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val value = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        spinner2?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val value = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        return view
    }

    private fun showDateTimePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar[Calendar.YEAR]
        val month = calendar[Calendar.MONTH]
        val day = calendar[Calendar.DAY_OF_MONTH]

        val datePickerDialog = DatePickerDialog(
            requireActivity(),
            { view: DatePicker?, year1: Int, monthOfYear: Int, dayOfMonth: Int ->
                btnDateTimePicker?.text = "$dayOfMonth/${monthOfYear + 1}/$year1"
            }, year, month, day
        )

        datePickerDialog.show()
    }

    private fun startProgressBar() {
        val duration = 3000
        val interval = 100
        val steps = duration / interval
        val increment = 100.0f / steps

        handler?.postDelayed(object : Runnable {
            var progress: Float = 0f

            override fun run() {
                progress += increment
                progressBar?.progress = progress.toInt()

                if (progress >= 100) {
                    progressBar?.visibility = View.INVISIBLE
                    val intent = Intent(requireContext(), BusActivity::class.java)
                    startActivity(intent)
                } else {
                    handler?.postDelayed(this, interval.toLong())
                }
            }
        }, interval.toLong())
    }
}
