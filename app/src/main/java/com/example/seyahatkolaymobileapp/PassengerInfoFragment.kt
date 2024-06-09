package com.example.busbookingapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.seyahatkolaymobileapp.BusActivity
import com.example.seyahatkolaymobileapp.MyTripsFragment
import com.example.seyahatkolaymobileapp.R

class PassengerInfoFragment : Fragment() {

    private lateinit var nameEditText: EditText
    private lateinit var genderRadioGroup: RadioGroup
    private lateinit var ageEditText: EditText
    private lateinit var cardNumberEditText: EditText
    private lateinit var expirationEditText: EditText
    private lateinit var cvvEditText: EditText
    private lateinit var addPassengerButton: Button
    private lateinit var passengersLayout: LinearLayout
    private lateinit var payButton: Button
    private lateinit var textViewPassengers: TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_passenger_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        textViewPassengers= view.findViewById(R.id.textViewPassengers)
        nameEditText = view.findViewById(R.id.editTextName)
        genderRadioGroup = view.findViewById(R.id.radioGroupGender)
        ageEditText = view.findViewById(R.id.editTextAge)
        cardNumberEditText = view.findViewById(R.id.editTextCardNumber)
        expirationEditText = view.findViewById(R.id.editTextExpiration)
        cvvEditText = view.findViewById(R.id.editTextCVV)
        addPassengerButton =view.findViewById(R.id.buttonAddPassenger)
        passengersLayout =view.findViewById(R.id.linearLayoutPassengers)
        payButton = view.findViewById(R.id.buttonPay)

        addPassengerButton.setOnClickListener {
            addPassenger()
        }

        payButton.setOnClickListener {
            showSuccessDialog("Ödeme Başarılı!", navigate = true)
        }
    }

    private fun addPassenger() {
        val name = nameEditText.text.toString()
        val age = ageEditText.text.toString()
        val genderId = genderRadioGroup.checkedRadioButtonId
        val gender = if (genderId != -1) view?.findViewById<RadioButton>(genderId)?.text.toString() else ""

        if (name.isNotEmpty() && age.isNotEmpty() && gender.isNotEmpty()) {
            val passengerInfo = TextView(context).apply {
                text = "$name, $age, $gender"
            }
            passengersLayout.addView(passengerInfo)
            textViewPassengers.visibility = View.VISIBLE

            showSuccessDialog("Yolcu Başarıyla Eklendi")
        } else {
            // Gerekli alanların doldurulmasını sağlayın
        }
    }

    private fun showSuccessDialog(message: String, navigate: Boolean = false) {
        val builder = AlertDialog.Builder(requireContext())
        val dialogView = layoutInflater.inflate(R.layout.dialog_success, null)
        builder.setView(dialogView)
        val alertDialog = builder.create()

        dialogView.findViewById<TextView>(R.id.textViewSuccessMessage).text = message
        val okButton = dialogView.findViewById<Button>(R.id.buttonOk)
        okButton.apply {
            text = "OK"
            setBackgroundResource(R.drawable.button_success)
            setTextColor(resources.getColor(android.R.color.white))
        }
        okButton.setOnClickListener {
            alertDialog.dismiss()
            val fragment = MyTripsFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }

        alertDialog.show()
    }

}
