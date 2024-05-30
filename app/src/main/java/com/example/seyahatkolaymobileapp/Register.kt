package com.example.seyahatkolaymobileapp

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Register : AppCompatActivity() {
    private var mediaPlayer: MediaPlayer? = null

    private lateinit var textView9: TextView
    private lateinit var txtCountDown: TextView
    private lateinit var txtUserName: EditText
    private lateinit var txtPassword: EditText
    private lateinit var txtRegRetypePass: EditText
    private lateinit var btnRegister: Button
    private var mAuth: FirebaseAuth? = null
    private var firestore: FirebaseFirestore? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mediaPlayer = MediaPlayer.create(this, R.raw.engine)
        this.enableEdgeToEdge()
        setContentView(R.layout.register)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v: View, insets: WindowInsetsCompat ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        textView9 = findViewById(R.id.textView9)
        txtCountDown = findViewById(R.id.txtCountDown)
        txtUserName = findViewById(R.id.txtRegUsername)
        txtPassword = findViewById(R.id.txtRegPassword)
        txtRegRetypePass = findViewById(R.id.txtRegRetypePass)
        btnRegister = findViewById(R.id.btnRegister)

        mAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        txtCountDown.visibility = View.INVISIBLE
        textView9.visibility = View.INVISIBLE

        btnRegister.setOnClickListener {
            val userName = txtUserName.text.toString()
            val password = txtPassword.text.toString()
            val retypePassword = txtRegRetypePass.text.toString()
            mediaPlayer?.start()
            if (userName.isNotEmpty() && password.isNotEmpty() && retypePassword.isNotEmpty()) {
                if (password == retypePassword) {
                    txtCountDown.visibility = View.VISIBLE
                    textView9.visibility = View.VISIBLE
                    mAuth?.createUserWithEmailAndPassword(userName, password)
                        ?.addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                val user = mAuth?.currentUser
                                user?.let {
                                    saveUserDataToFirestore(it.uid, userName, password)
                                    Toast.makeText(this@Register, "Kayıt Başarılı", Toast.LENGTH_SHORT)
                                        .show()
                                    startCountdownTimer()
                                }
                            } else {
                                Toast.makeText(
                                    this@Register,
                                    task.exception?.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                } else {
                    Toast.makeText(this@Register, "Şifreler Eşleşmiyor..", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                Toast.makeText(
                    this@Register,
                    "Kullanıcı adı, parola boş bırakılamaz..",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun saveUserDataToFirestore(userId: String, username: String, password: String) {
        val user = hashMapOf(
            "username" to username,
            "password" to password
        )

        // Add a new document with a generated ID
        firestore?.collection("users")
            ?.document(userId)
            ?.set(user)
    }

    private fun startCountdownTimer() {
        object : CountDownTimer(3000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = millisUntilFinished / 1000
                txtCountDown.text = seconds.toString()
            }

            override fun onFinish() {
                txtCountDown.visibility = View.INVISIBLE
                textView9.visibility = View.INVISIBLE
                val intent = Intent(this@Register, Login::class.java)
                startActivity(intent)
                finish()
            }
        }.start()
    }
}
