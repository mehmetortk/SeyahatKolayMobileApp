package com.example.seyahatkolaymobileapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import java.util.Calendar;

public class TicketFragment extends Fragment {
    Handler handler;

    Spinner spinner1, spinner2;
    Button btnDateTimePicker, btnChangeDirection, searchButton;
    ProgressBar progressBar;
    String[] sehirler = {"Şehir Seçiniz", "Adana", "Adıyaman", "Afyon", "Ağrı", "Amasya", "Ankara", "Antalya", "Artvin",
            "Aydın", "Balıkesir", "Bilecik", "Bingöl", "Bitlis", "Bolu", "Burdur", "Bursa", "Çanakkale",
            "Çankırı", "Çorum", "Denizli", "Diyarbakır", "Edirne", "Elazığ", "Erzincan", "Erzurum ", "Eskişehir",
            "Gaziantep", "Giresun", "Gümüşhane", "Hakkari", "Hatay", "Isparta", "Mersin", "İstanbul", "İzmir",
            "Kars", "Kastamonu", "Kayseri", "Kırklareli", "Kırşehir", "Kocaeli", "Konya", "Kütahya ", "Malatya",
            "Manisa", "Kahramanmaraş", "Mardin", "Muğla", "Muş", "Nevşehir", "Niğde", "Ordu", "Rize", "Sakarya",
            "Samsun", "Siirt", "Sinop", "Sivas", "Tekirdağ", "Tokat", "Trabzon  ", "Tunceli", "Şanlıurfa", "Uşak",
            "Van", "Yozgat", "Zonguldak", "Aksaray", "Bayburt ", "Karaman", "Kırıkkale", "Batman", "Şırnak",
            "Bartın", "Ardahan", "Iğdır", "Yalova", "Karabük ", "Kilis", "Osmaniye ", "Düzce"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ticket, container, false);
        progressBar = view.findViewById(R.id.progressBar);
        btnDateTimePicker = view.findViewById(R.id.btnDateTimePicker);
        btnChangeDirection = view.findViewById(R.id.btnChangeDirection);
        spinner1 = view.findViewById(R.id.spinner1);
        handler = new Handler();
        spinner2 = view.findViewById(R.id.spinner2);
        searchButton = view.findViewById(R.id.searchButton);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_item, sehirler);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter);
        spinner2.setAdapter(adapter);
        btnChangeDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int spinner1Position = spinner1.getSelectedItemPosition();
                int spinner2Position = spinner2.getSelectedItemPosition();

                spinner1.setSelection(spinner2Position);
                spinner2.setSelection(spinner1Position);
            }
        });

        btnDateTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePicker(v);
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                startProgressBar();
            }
        });

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String value = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String value = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

    public void showDateTimePicker(View v) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireActivity(),
                (view, year1, monthOfYear, dayOfMonth) -> btnDateTimePicker.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1), year, month, day);

        datePickerDialog.show();
    }

    private void startProgressBar() {
        final int duration = 3000;

        final int interval = 100;

        final int steps = duration / interval;

        final float increment = 100.0f / steps;

        handler.postDelayed(new Runnable() {
            float progress = 0;

            @Override
            public void run() {
                progress += increment;
                progressBar.setProgress((int) progress);

                if (progress >= 100) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Intent intent = new Intent(requireContext(), BusActivity.class);
                    startActivity(intent);
                } else {
                    handler.postDelayed(this, interval);
                }
            }
        }, interval);
    }

}
