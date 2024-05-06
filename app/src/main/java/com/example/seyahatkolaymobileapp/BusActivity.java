package com.example.seyahatkolaymobileapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class BusActivity extends AppCompatActivity {
    public Bus bus1= new Bus();
    EditText firmName;
    EditText from_city;
    EditText to_city;
    EditText price;
    EditText date;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bus);
        bus1.date="11.00"; bus1.firm="Kamil Koç"; bus1.price="400TL";bus1.nereden(getString());
        firmName=findViewById(R.id.firm_name);
        from_city=findViewById(R.id.from_city);
        to_city=findViewById(R.id.to_city);
        date=findViewById(R.id.date);
        price=findViewById(R.id.price);
        firmName.setText(bus1.firm);
        from_city.setText();
        to_city.setText(to_city.getText());
        date.setText(date.getText());
        price.setText(price.getText());






    }
}