package com.example.seyahatkolaymobileapp;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class UserProfileFragment extends Fragment {
    private FirebaseFirestore firestore;

    private TextView selectedDateText;
    private EditText nameSurnametxt, tcKimliktxt, phonetxt;
    private Button pickDateButton,btnSave;
    private Calendar calendar;

    private RadioButton radio_button_male,getRadio_button_female;

    public UserProfileFragment() {
        // Required empty public constructor
    }

    public static UserProfileFragment newInstance() {
        return new UserProfileFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        firestore = FirebaseFirestore.getInstance();

        selectedDateText = view.findViewById(R.id.selected_date_text);
        pickDateButton = view.findViewById(R.id.birthDatePickerbtn);
        nameSurnametxt = view.findViewById(R.id.nameSurnametxt);
        tcKimliktxt = view.findViewById(R.id.tcKimliktxt);
        phonetxt = view.findViewById(R.id.phonetxt);
        btnSave= (Button) view.findViewById(R.id.btnSave);
        radio_button_male= view.findViewById(R.id.radio_button_male);
        getRadio_button_female=view.findViewById(R.id.radio_button_female);

        calendar = Calendar.getInstance();

        pickDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        RadioGroup radioGroupGender = view.findViewById(R.id.radio_group_gender);
        radioGroupGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButtonFemale = view.findViewById(R.id.radio_button_female);
                RadioButton radioButtonMale = view.findViewById(R.id.radio_button_male);

                if (checkedId == R.id.radio_button_female) {
                    radioButtonMale.setChecked(false);
                } else if (checkedId == R.id.radio_button_male) {
                    radioButtonFemale.setChecked(false);
                }

                RadioButton checkedRadioButton = view.findViewById(checkedId);
                if (checkedRadioButton != null) {
                    Toast.makeText(getActivity(), "Selected gender: " + checkedRadioButton.getText(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kullanıcı verilerini al
                String nameSurname = nameSurnametxt.getText().toString().trim();
                String tcKimlik = tcKimliktxt.getText().toString().trim();
                String phone = phonetxt.getText().toString().trim();
                String selectedGender = "";
                if (radio_button_male.isChecked()) {
                    selectedGender = "Male";
                } else if (getRadio_button_female.isChecked()) {
                    selectedGender = "Female";
                }
                String birthDate = selectedDateText.getText().toString();
                if (birthDate.isEmpty()) {
                    Toast.makeText(getActivity(), "Doğum tarihi seçiniz..", Toast.LENGTH_SHORT).show();
                    return;
                }


                saveUserDataToFirestore(nameSurname, tcKimlik, phone, selectedGender, birthDate);

            }
        });

        return view;
    }

    private void saveUserDataToFirestore(String nameSurname, String tcKimlik, String phone, String gender, String birthDate) {
        Map<String, Object> user = new HashMap<>();
        user.put("nameSurname", nameSurname);
        user.put("tcKimlik", tcKimlik);
        user.put("phone", phone);
        user.put("gender", gender);
        user.put("birthDate", birthDate);

        firestore.collection("user_info")
                .document()
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getActivity(), "User data saved successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Failed to save user data", Toast.LENGTH_SHORT).show();
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

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getActivity(),
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis()); // Maksimum tarih bugünün tarihi
        datePickerDialog.show();
    }

    private void updateDate() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

        String selectedDate = sdf.format(calendar.getTime());
        selectedDateText.setText(selectedDate);
        pickDateButton.setText(selectedDate);
    }
}
