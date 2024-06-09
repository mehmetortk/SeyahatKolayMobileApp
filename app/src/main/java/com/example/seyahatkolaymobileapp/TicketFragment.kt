package com.example.seyahatkolaymobileapp

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.SearchView
import com.example.busbookingapp.PassengerInfoFragment
import java.util.Calendar


class TicketFragment : Fragment() {
    private lateinit var handler: Handler

    private lateinit var spinner1: Spinner
    private lateinit var spinner2: Spinner
    private lateinit var btnDateTimePicker: Button
    private lateinit var btnChangeDirection: Button
    private lateinit var searchButton: Button
    private lateinit var progressBar: ProgressBar
    private val sehirler: Array<String> = arrayOf(
        "Şehir Seçiniz",
        "Adana","Adıyaman", "Afyon", "Ağrı", "Amasya", "Ankara", "Antalya", "Artvin",
        "Aydın", "Balıkesir","Bilecik", "Bingöl", "Bitlis", "Bolu", "Burdur", "Bursa", "Çanakkale",
        "Çankırı", "Çorum","Denizli","Diyarbakır", "Edirne", "Elazığ", "Erzincan", "Erzurum ", "Eskişehir",
        "Gaziantep", "Giresun","Gümüşhane", "Hakkari", "Hatay", "Isparta", "Mersin", "İstanbul", "İzmir",
        "Kars", "Kastamonu", "Kayseri","Kırklareli", "Kırşehir", "Kocaeli", "Konya", "Kütahya ", "Malatya",
        "Manisa", "Kahramanmaraş", "Mardin", "Muğla", "Muş", "Nevşehir", "Niğde", "Ordu", "Rize", "Sakarya",
        "Samsun", "Siirt", "Sinop", "Sivas", "Tekirdağ", "Tokat", "Trabzon  ", "Tunceli", "Şanlıurfa", "Uşak",
        "Van", "Yozgat", "Zonguldak", "Aksaray", "Bayburt ", "Karaman", "Kırıkkale", "Batman", "Şırnak",
        "Bartın", "Ardahan", "Iğdır", "Yalova", "Karabük ", "Kilis", "Osmaniye ", "Düzce"
    )
    private lateinit var filteredSehirler: ArrayList<String>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_ticket, container, false)
        progressBar = view.findViewById(R.id.progressBar)
        btnDateTimePicker = view.findViewById(R.id.btnDateTimePicker)
        btnChangeDirection = view.findViewById(R.id.btnChangeDirection)
        spinner1 = view.findViewById(R.id.spinner1)
        spinner2 = view.findViewById(R.id.spinner2)
        searchButton = view.findViewById(R.id.searchButton)
        handler = Handler()

        filteredSehirler = ArrayList(sehirler.asList())

        val adapter = CustomArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, filteredSehirler)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner1.adapter = adapter
        spinner2.adapter = adapter

        btnChangeDirection.setOnClickListener {
            val spinner1Position = spinner1.selectedItemPosition
            val spinner2Position = spinner2.selectedItemPosition

            spinner1.setSelection(spinner2Position)
            spinner2.setSelection(spinner1Position)
        }

        val shrinkAnim = AnimationUtils.loadAnimation(context, R.anim.button_shrink)
        val expandAnim = AnimationUtils.loadAnimation(context, R.anim.button_expand)

        btnChangeDirection.setOnTouchListener { v, event ->
            v.startAnimation(shrinkAnim)
            v.postDelayed({ v.startAnimation(expandAnim) }, 50)
            false
        }

        btnDateTimePicker.setOnTouchListener { v, event ->
            v.startAnimation(shrinkAnim)
            v.postDelayed({ v.startAnimation(expandAnim) }, 50)
            false
        }

        searchButton.setOnTouchListener { v, event ->
            v.startAnimation(shrinkAnim)
            v.postDelayed({ v.startAnimation(expandAnim) }, 50)
            false
        }
        progressBar.visibility=View.INVISIBLE
        btnDateTimePicker.setOnClickListener { showDateTimePicker() }

        searchButton.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            startProgressBar()
            val fragment = PassengerInfoFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }

        spinner1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Seçilen şehri al
                val selectedCity = sehirler[position]
                Toast.makeText(requireContext(), "Seçilen Şehir: $selectedCity", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Hiçbir şey seçilmediğinde yapılacak işlemler
            }
        }

        return view
    }

    private fun showDateTimePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(), { view, year, monthOfYear, dayOfMonth ->
            val selectedDate = "$dayOfMonth/${monthOfYear + 1}/$year"
            btnDateTimePicker.text = selectedDate
        }, year, month, day)

        datePickerDialog.show()
    }

    private fun startProgressBar() {
        handler.postDelayed(object : Runnable {
            var progress = 0
            override fun run() {
                progress += 1
                progressBar.progress = progress
                if (progress < 100) {
                    handler.postDelayed(this, 30)
                } else {
                    // 100% tamamlandığında yapılacak işlemler
                    progressBar.visibility = View.INVISIBLE
                }
            }
        }, 30)
    }

    inner class CustomArrayAdapter(context: Context, resource: Int, objects: List<String>) :
        ArrayAdapter<String>(context, resource, objects) {
        private val originalList = ArrayList(objects)
        private val filteredList = ArrayList(objects)

        override fun getCount(): Int {
            return if (filteredList.size > 7) 7 else filteredList.size
        }


        override fun getItem(position: Int): String? {
            return filteredList[position]
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var view = convertView
            if (view == null) {
                val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                view = inflater.inflate(android.R.layout.simple_spinner_item, parent, false)
            }
            val textView = view!!.findViewById<TextView>(android.R.id.text1)
            textView.text = getItem(position)
            return view
        }

        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
            var view = convertView
            if (position == 0) {
                val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                view = inflater.inflate(R.layout.spinner_search_layout, parent, false)

                val searchView = view!!.findViewById<SearchView>(R.id.searchView)
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        filter(newText ?: "")
                        return true
                    }
                })

                // SearchView'in herhangi bir yerine tıklanınca aktif olması için tıklama olayını ayarlıyoruz.
                searchView.setOnClickListener {
                    searchView.isIconified = false
                }
            } else {
                val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                view = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false)
                val textView = view!!.findViewById<TextView>(android.R.id.text1)
                textView.text = getItem(position)
            }
            return view
        }

        private fun filter(query: String) {
            filteredList.clear()
            if (query.isEmpty()) {
                filteredList.addAll(originalList)
            } else {
                for (item in originalList) {
                    if (item.toLowerCase().startsWith(query.toLowerCase())) {
                        filteredList.add(item)
                    }
                }
                filteredList.sort() // Alfabetik sıralama
            }
            notifyDataSetChanged()
        }
    }


}
