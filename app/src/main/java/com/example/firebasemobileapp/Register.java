package com.example.firebasemobileapp;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

public class Register extends AppCompatActivity {
EditText txtUserName,txtPassword;
Button btnRegister;
FirebaseAuth mAuth;


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

        txtUserName=findViewById(R.id.txtRegUsername);
        txtPassword=findViewById(R.id.txtRegPassword);
        btnRegister=findViewById(R.id.btnRegister);
        mAuth=FirebaseAuth.getInstance();
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String UserName=txtUserName.getText().toString();
                String Password=txtPassword.getText().toString();

                if (!UserName.isEmpty()&&!Password.isEmpty()){
                    mAuth.createUserWithEmailAndPassword(UserName,Password).addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful())
                                Toast.makeText(Register.this,"Registration Successful",Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(Register.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else
                    Toast.makeText(Register.this,"Username and Password cannot be left blank",Toast.LENGTH_SHORT).show();
            }
        });

    }
}