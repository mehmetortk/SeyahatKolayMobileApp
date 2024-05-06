package com.example.seyahatkolaymobileapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Login extends AppCompatActivity {
    FirebaseFirestore firestore;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        firestore = FirebaseFirestore.getInstance();

        EditText txtUsername, txtPassword;
        Button btnLogin, btnRegister;
        Vibrator vibrator;

        txtUsername = findViewById(R.id.txtUsername);
        txtPassword = findViewById(R.id.txtRegPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnReg);
        vibrator=(Vibrator) getSystemService(VIBRATOR_SERVICE);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = txtUsername.getText().toString();
                String password = txtPassword.getText().toString();

                firestore.collection("users")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    boolean userFound = false;
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String actualUsername = document.getString("username");
                                        String actualPassword = document.getString("password");
                                        if (document.exists() && actualPassword != null && actualPassword.equals(password) && actualUsername != null && actualUsername.equals(username)) {
                                            Toast.makeText(Login.this, "Success", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(Login.this, MainActivity.class);
                                            startActivity(intent);
                                            userFound = true;
                                            break; // Kullanıcı bulunduğunda döngüyü sonlandır
                                        }
                                    }
                                    if (!userFound) {
                                        Toast.makeText(Login.this, "User not found or incorrect password", Toast.LENGTH_SHORT).show();

                                    }
                                } else {
                                    Exception exception = task.getException();
                                    if (exception != null) {
                                        Log.e("FirestoreError", "Error retrieving user data: " + exception.getMessage());
                                        vibrator.vibrate(450);
                                    }
                                    Toast.makeText(Login.this, "Error retrieving data. Please try again later", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}
