package com.example.seyahatkolaymobileapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    TextView textView9, txtCountDown;
    EditText txtUserName, txtPassword, txtRegRetypePass;
    Button btnRegister;
    FirebaseAuth mAuth;
    FirebaseFirestore firestore;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txtUserName = findViewById(R.id.txtRegUsername);
        txtPassword = findViewById(R.id.txtRegPassword);
        btnRegister = findViewById(R.id.btnRegister);
        txtCountDown = findViewById(R.id.txtCountDown);
        txtRegRetypePass = findViewById(R.id.txtRegRetypePass);
        textView9 = findViewById(R.id.textView9);
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        txtCountDown.setVisibility(View.INVISIBLE);
        textView9.setVisibility(View.INVISIBLE);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = txtUserName.getText().toString();
                String password = txtPassword.getText().toString();
                String retypePassword = txtRegRetypePass.getText().toString();

                // Şifre ve şifre tekrarını kontrol et
                if (!userName.isEmpty() && !password.isEmpty() && !retypePassword.isEmpty()) {
                    if (password.equals(retypePassword)) {
                        // Şifreler eşleşirse kayıt işlemine devam et
                        txtCountDown.setVisibility(View.VISIBLE);
                        textView9.setVisibility(View.VISIBLE);
                        mAuth.createUserWithEmailAndPassword(userName, password).addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    saveUserDataToFirestore(user.getUid(), userName, password);
                                    Toast.makeText(Register.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                    startCountdownTimer();
                                } else {
                                    Toast.makeText(Register.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        // Şifreler eşleşmezse hata mesajı göster
                        Toast.makeText(Register.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Register.this, "Username, Password, and Retyped Password cannot be left blank", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void saveUserDataToFirestore(String userId, String username, String password) {
        // Create a new user object with username and any other necessary data
        Map<String, Object> user = new HashMap<>();
        user.put("username", username);
        user.put("password", password);
        // Add a new document with a generated ID
        firestore.collection("users")
                .document(userId)
                .set(user);
    }

    private void startCountdownTimer() {
        // 3 saniyelik geri sayım zamanlayıcısı başlat
        new CountDownTimer(3000, 1000) {
            public void onTick(long millisUntilFinished) {
                long seconds = millisUntilFinished / 1000;
                txtCountDown.setText(String.valueOf(seconds)); // Kalan süreyi textView9'a yaz
            }

            public void onFinish() {
                txtCountDown.setVisibility(View.INVISIBLE);
                textView9.setVisibility(View.INVISIBLE);
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
                finish();
            }
        }.start();
    }


}