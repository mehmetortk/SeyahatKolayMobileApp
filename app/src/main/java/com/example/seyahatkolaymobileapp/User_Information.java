package com.example.seyahatkolaymobileapp;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class User_Information extends AppCompatActivity {

    private TextView selectedDateText;
    EditText nameSurnametxt,tcKimliktxt,phonetxt;
    private Button pickDateButton;

    private Calendar calendar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_information);

        selectedDateText = findViewById(R.id.selected_date_text);
        pickDateButton = findViewById(R.id.birthDatePickerbtn);
        nameSurnametxt=findViewById(R.id.nameSurnametxt);
        tcKimliktxt=findViewById(R.id.tcKimliktxt);
        phonetxt=findViewById(R.id.phonetxt);

        calendar = Calendar.getInstance();

        pickDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        RadioGroup radioGroupGender = findViewById(R.id.radio_group_gender);
        radioGroupGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButtonFemale = findViewById(R.id.radio_button_female);
                RadioButton radioButtonMale = findViewById(R.id.radio_button_male);

                // Sadece seçili olan RadioButton'ı kontrol et
                if (checkedId == R.id.radio_button_female) {
                    radioButtonMale.setChecked(false); // Diğerini seçilmemiş yap
                } else if (checkedId == R.id.radio_button_male) {
                    radioButtonFemale.setChecked(false); // Diğerini seçilmemiş yap
                }

                RadioButton checkedRadioButton = findViewById(checkedId);
                if (checkedRadioButton != null) {
                    Toast.makeText(User_Information.this, "Seçilen cinsiyet: " + checkedRadioButton.getText(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showDatePickerDialog() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDate();
            }
        };

        new DatePickerDialog(
                this,
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

    private void updateDate() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

        String selectedDate = sdf.format(calendar.getTime());
        selectedDateText.setText(selectedDate);
        pickDateButton.setText(selectedDate); // Bu satırda butonun metnini güncelliyoruz.
    }
}
