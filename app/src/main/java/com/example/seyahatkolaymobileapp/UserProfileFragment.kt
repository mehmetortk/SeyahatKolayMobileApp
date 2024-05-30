package com.example.seyahatkolaymobileapp

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class UserProfileFragment : Fragment() {
    private var firestore: FirebaseFirestore? = null

    private lateinit var selectedDateText: TextView
    private lateinit var nameSurnametxt: EditText
    private lateinit var tcKimliktxt: EditText
    private lateinit var phonetxt: EditText
    private lateinit var pickDateButton: Button
    private lateinit var btnSave: Button
    private lateinit var radio_button_male: RadioButton
    private lateinit var radio_button_female: RadioButton
    private val calendar: Calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_profile, container, false)
        firestore = FirebaseFirestore.getInstance()

        selectedDateText = view.findViewById(R.id.selected_date_text)
        pickDateButton = view.findViewById(R.id.birthDatePickerbtn)
        nameSurnametxt = view.findViewById(R.id.nameSurnametxt)
        tcKimliktxt = view.findViewById(R.id.tcKimliktxt)
        phonetxt = view.findViewById(R.id.phonetxt)
        btnSave = view.findViewById(R.id.btnSave)
        radio_button_male = view.findViewById(R.id.radio_button_male)
        radio_button_female = view.findViewById(R.id.radio_button_female)

        pickDateButton.setOnClickListener { showDatePickerDialog() }

        val radioGroupGender = view.findViewById<RadioGroup>(R.id.radio_group_gender)
        radioGroupGender.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.radio_button_female) {
                radio_button_male.isChecked = false
            } else if (checkedId == R.id.radio_button_male) {
                radio_button_female.isChecked = false
            }

            val checkedRadioButton = view.findViewById<RadioButton>(checkedId)
            checkedRadioButton?.let {
                Toast.makeText(
                    activity,
                    "Selected gender: ${it.text}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        btnSave.setOnClickListener { saveUserData() }

        return view
    }

    private fun saveUserData() {
        val nameSurname = nameSurnametxt.text.toString().trim()
        val tcKimlik = tcKimliktxt.text.toString().trim()
        val phone = phonetxt.text.toString().trim()
        val selectedGender = if (radio_button_male.isChecked) "Male" else "Female"
        val birthDate = selectedDateText.text.toString()
        if (birthDate.isEmpty()) {
            Toast.makeText(activity, "Doğum tarihi seçiniz..", Toast.LENGTH_SHORT).show()
            return
        }
        saveUserDataToFirestore(nameSurname, tcKimlik, phone, selectedGender, birthDate)
    }

    private fun saveUserDataToFirestore(
        nameSurname: String,
        tcKimlik: String,
        phone: String,
        gender: String,
        birthDate: String
    ) {
        val user = hashMapOf(
            "nameSurname" to nameSurname,
            "tcKimlik" to tcKimlik,
            "phone" to phone,
            "gender" to gender,
            "birthDate" to birthDate
        )

        firestore?.collection("user_info")
            ?.add(user)
            ?.addOnSuccessListener {
                Toast.makeText(
                    activity,
                    "User data saved successfully",
                    Toast.LENGTH_SHORT
                ).show()
            }
            ?.addOnFailureListener {
                Toast.makeText(
                    activity,
                    "Failed to save user data",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun showDatePickerDialog() {
        val dateSetListener = OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDate()
        }

        val datePickerDialog = DatePickerDialog(
            requireActivity(),
            dateSetListener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    private fun updateDate() {
        val myFormat = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.getDefault())

        val selectedDate = sdf.format(calendar.time)
        selectedDateText.text = selectedDate
        pickDateButton.text = selectedDate
    }

    companion object {
        fun newInstance(): UserProfileFragment {
            return UserProfileFragment()
        }
    }
}
