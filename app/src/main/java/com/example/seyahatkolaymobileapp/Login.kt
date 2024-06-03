package com.example.seyahatkolaymobileapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Vibrator
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class Login : AppCompatActivity() {
    var firestore: FirebaseFirestore? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        firestore = FirebaseFirestore.getInstance()
        val shrinkAnim = AnimationUtils.loadAnimation(this, R.anim.button_shrink)
        val expandAnim = AnimationUtils.loadAnimation(this, R.anim.button_expand)


        val txtUsername = findViewById<EditText>(R.id.txtUsername)
        val txtPassword = findViewById<EditText>(R.id.txtRegPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnRegister = findViewById<Button>(R.id.btnReg)
        val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        btnLogin.setOnTouchListener { v, event ->
            v.startAnimation(shrinkAnim)
            v.postDelayed({ v.startAnimation(expandAnim) }, 50)
            false
        }

        btnRegister.setOnTouchListener { v, event ->
            v.startAnimation(shrinkAnim)
            v.postDelayed({ v.startAnimation(expandAnim) }, 50)
            false
        }
        btnRegister.setOnClickListener {
            val intent = Intent(this@Login, Register::class.java)
            startActivity(intent)
        }

        btnLogin.setOnClickListener {
            val username = txtUsername.text.toString()
            val password = txtPassword.text.toString()
            firestore!!.collection("users")
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        var userFound = false
                        for (document in task.result) {
                            val actualUsername = document.getString("username")
                            val actualPassword = document.getString("password")
                            if (document.exists() && actualPassword != null && actualPassword == password && actualUsername != null && actualUsername == username) {
                                Toast.makeText(this@Login, "Giriş Başarılı", Toast.LENGTH_SHORT)
                                    .show()
                                val intent = Intent(this@Login, MainActivity::class.java)
                                startActivity(intent)
                                userFound = true
                                break // Kullanıcı bulunduğunda döngüyü sonlandır
                            }
                        }
                        if (!userFound) {
                            Toast.makeText(
                                this@Login,
                                "Kullanıcı adı veya parola yanlış",
                                Toast.LENGTH_SHORT
                            ).show()
                            vibrator.vibrate(450)
                        }
                    } else {
                        val exception = task.exception
                        if (exception != null) {
                            Log.e(
                                "FirestoreError",
                                "Error retrieving user data: " + exception.message
                            )
                            vibrator.vibrate(450)
                        }
                        Toast.makeText(
                            this@Login,
                            "Veri çekilemiyor. Lütfen tekrar deneyiniz.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }
}
