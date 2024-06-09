package com.example.seyahatkolaymobileapp

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.seyahatkolaymobileapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(
            layoutInflater
        )
        setContentView(binding!!.root)
        replaceFragment(TicketFragment())

        binding!!.bottomNavigationView.setOnItemSelectedListener { menuItem: MenuItem ->
            if (menuItem.itemId == R.id.fragment_ticket) {
                replaceFragment(TicketFragment())
            } else if (menuItem.itemId == R.id.fragment_user_profile) {
                replaceFragment(UserProfileFragment())
            } else if (menuItem.itemId == R.id.fragment_aboutus) {
                replaceFragment(`AboutusFragment`())
            }
            true
        }
    }

    fun replaceFragment(fragment: Fragment?) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment!!)
        fragmentTransaction.commit()
    }
}
