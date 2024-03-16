package com.example.firebasemobileapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login extends AppCompatActivity {
    FirebaseFirestore firestore;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        firestore = FirebaseFirestore.getInstance();

        EditText txtUsername, txtPassword ;
        Button btnLogin,btnRegister;

        txtUsername = findViewById(R.id.txtUsername);
        txtPassword = findViewById(R.id.txtRegPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister=findViewById(R.id.btnReg);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Login.this,Register.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = txtUsername.getText().toString();
                String password = txtPassword.getText().toString();

                firestore.collection("users").document("user1")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        String actualPassword = document.getString("password");
                                        if (actualPassword != null && actualPassword.equals(password)) {
                                            Toast.makeText(Login.this, "Success", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(Login.this, MainPage.class);
                                            startActivity(intent);
                                        } else {
                                            Toast.makeText(Login.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(Login.this, "User not found!!", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Exception exception = task.getException();
                                    if (exception != null) {
                                        Log.e("FirestoreError", "Error retrieving user data: " + exception.getMessage());
                                    }
                                    Toast.makeText(Login.this, "Error retrieving data. Please try again later", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}
