package com.example.busapp

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.busapp.com.example.seyahatkolaymobileapp.SeferAdapter
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*
import com.example.seyahatkolaymobileapp.R

class trip : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var seferAdapter: SeferAdapter
    private lateinit var selectedDate: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trips)

        database = FirebaseDatabase.getInstance().reference.child("seferler")
        recyclerView = findViewById(R.id.recycler_view)
        selectedDate = findViewById(R.id.selected_date)

        recyclerView.layoutManager = LinearLayoutManager(this)
        seferAdapter = SeferAdapter(mutableListOf()) { sefer ->
            // On click listener for item
            showSeferDetail(sefer)
        }
        recyclerView.adapter = seferAdapter

        val currentDate = SimpleDateFormat("dd MMMM EEEE", Locale.getDefault()).format(Date())
        selectedDate.text = currentDate

        fetchSeferler(currentDate)

        findViewById<Button>(R.id.prev_day).setOnClickListener {
            updateDate(-1)
        }
        findViewById<Button>(R.id.next_day).setOnClickListener {
            updateDate(1)
        }
    }

    private fun fetchSeferler(date: String) {
        database.orderByChild("tarih").equalTo(date).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val seferler = mutableListOf<Sefer>()
                for (seferSnapshot in snapshot.children) {
                    val sefer = seferSnapshot.getValue(Sefer::class.java)
                    if (sefer != null) {
                        seferler.add(sefer)
                    }
                }
                seferAdapter.updateSeferler(seferler)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@trip, "Hata: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateDate(days: Int) {
        val sdf = SimpleDateFormat("dd MMMM EEEE", Locale.getDefault())
        val currentDate = sdf.parse(selectedDate.text.toString())
        val calendar = Calendar.getInstance()
        calendar.time = currentDate!!
        calendar.add(Calendar.DAY_OF_YEAR, days)
        val newDate = sdf.format(calendar.time)
        selectedDate.text = newDate
        fetchSeferler(newDate)
    }

    private fun showSeferDetail(sefer: Sefer) {
        val seferFragment = SeferFragment.newInstance(sefer)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, seferFragment)
            .addToBackStack(null)
            .commit()
    }
}
