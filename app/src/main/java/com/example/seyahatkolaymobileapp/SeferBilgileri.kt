package com.example.busapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.seyahatkolaymobileapp.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SeferBilgileri : AppCompatActivity() {

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        database = FirebaseDatabase.getInstance().getReference("seferler")

        val sefer1 = Sefer("Ali Osman Ulusoy", "2023-06-09", "22:30", "900 TL", 2)
        val sefer2 = Sefer("Metro", "2023-06-09", "21:50", "750 TL", 1)
        val sefer3 = Sefer("Özlem", "2023-06-09", "23:00", "750 TL", 2)
        val sefer4 = Sefer("Pamukkale", "2023-06-09", "20:30", "850 TL", 3)
        val sefer5 = Sefer("Nilüfer", "2023-06-09", "19:30", "800 TL", 4)

        database.child("1").setValue(sefer1)
        database.child("2").setValue(sefer2)
        database.child("3").setValue(sefer3)
        database.child("4").setValue(sefer4)
        database.child("5").setValue(sefer5)
    }
}
