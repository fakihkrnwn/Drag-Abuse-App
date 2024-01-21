package com.example.drugabuseapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.drugabuseapp.models.DrugModel
import com.example.drugabuseapp.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class InsertionActivity : AppCompatActivity() {

    private lateinit var etDrugName: EditText
    private lateinit var etDrugDesc: EditText
    private lateinit var etSideEffect: EditText
    private lateinit var etTreatment: EditText
    private lateinit var btnSaveData: Button

    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insertion)

        etDrugName = findViewById(R.id.etDrugName)
        etDrugDesc = findViewById(R.id.etDrugDesc)
        etSideEffect = findViewById(R.id.etSideEffect)
        etTreatment = findViewById(R.id.etTreatment)
        btnSaveData = findViewById(R.id.btnSave)

        dbRef = FirebaseDatabase.getInstance().getReference("Drugs")

        btnSaveData.setOnClickListener {
            saveDrugData()
        }
    }

    private fun saveDrugData() {

        //getting values
        val drugName = etDrugName.text.toString()
        val drugDesc = etDrugDesc.text.toString()
        val drugSide =  etSideEffect.text.toString()
        val drugTreatment = etTreatment.text.toString()

        if (drugName.isEmpty()) {
            etDrugName.error = "Enter Drug Name"
        }
        if (drugDesc.isEmpty()) {
            etDrugDesc.error = "Description"
        }
        if (drugSide.isEmpty()) {
            etSideEffect.error = "Enter Side Effect"
        }
        if (drugTreatment.isEmpty()) {
            etTreatment.error = "Enter Treatment"
        }

        val drugId = dbRef.push().key!!

        val drug = DrugModel(drugId, drugName, drugDesc, drugSide, drugTreatment)
        //val employee = EmployeeModel(empId, empName, empAge, empSalary)

        dbRef.child(drugId).setValue(drug)
            .addOnCompleteListener {
                Toast.makeText(this, "Data inserted successfully", Toast.LENGTH_LONG).show()

                etDrugName.text.clear()
                etDrugDesc.text.clear()
                etSideEffect.text.clear()
                etTreatment.text.clear()


            }.addOnFailureListener { err ->
                Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()
            }

    }

}
