package com.example.drugabuseapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.drugabuseapp.R
import com.example.drugabuseapp.models.DrugModel
import com.google.firebase.database.FirebaseDatabase

class DrugDetailsActivity : AppCompatActivity() {
    private lateinit var tvDrugId: TextView
    private lateinit var tvDrugName: TextView
    private lateinit var tvDrugDesc: TextView
    private lateinit var tvDrugSide: TextView
    private lateinit var tvDrugTreatment: TextView
    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drug_details)

        initView()
        setValuesToViews()

        btnUpdate.setOnClickListener {
            openUpdateDialog (
                intent.getStringExtra("drugId").toString(),
                intent.getStringExtra("drugName").toString()
            )
        }

        btnDelete.setOnClickListener {
            deleteRecord (
                intent.getStringExtra("drugId").toString(),
            )
        }

    }

    private fun initView() {
        tvDrugId = findViewById(R.id.tvDrugId)
        tvDrugName = findViewById(R.id.tvDrugName)
        tvDrugDesc = findViewById(R.id.tvDrugDesc)
        tvDrugSide = findViewById(R.id.tvDrugSide)
        tvDrugTreatment = findViewById(R.id.tvDrugTreatment)

        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)
    }

    private fun setValuesToViews() {
        tvDrugId.text = intent.getStringExtra("drugId")
        tvDrugName.text = intent.getStringExtra("drugName")
        tvDrugDesc.text = intent.getStringExtra("drugDesc")
        tvDrugSide.text = intent.getStringExtra("drugSide")
        tvDrugTreatment.text = intent.getStringExtra("drugTreatment")

    }

    private fun openUpdateDialog (
        drugId: String, drugName: String
    ){
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.update_dialog, null)

        mDialog.setView(mDialogView)

        val etDrugName = mDialogView.findViewById<EditText>(R.id.etDrugName)
        val etDrugDesc = mDialogView.findViewById<EditText>(R.id.etDrugDesc)
        val etSideEffect = mDialogView.findViewById<EditText>(R.id.etSideEffect)
        val etTreatment = mDialogView.findViewById<EditText>(R.id.etTreatment)
        val btnUpdateData = mDialogView.findViewById<Button>(R.id.btnUpdateData)

        etDrugName.setText(intent.getStringExtra("drugName").toString())
        etDrugDesc.setText(intent.getStringExtra("drugDesc").toString())
        etSideEffect.setText(intent.getStringExtra("drugSide").toString())
        etTreatment.setText(intent.getStringExtra("drugTreatment").toString())

        mDialog.setTitle("Updating $drugName Record")

        val alertDialog = mDialog.create()
        alertDialog.show()

        btnUpdateData.setOnClickListener {
            updateDrugData(
                drugId,
                etDrugName.text.toString(),
                etDrugDesc.text.toString(),
                etSideEffect.text.toString(),
                etTreatment.text.toString()
            )

            Toast.makeText(applicationContext, "Drug Data Updated", Toast.LENGTH_LONG).show()

            //we are setting updated data to our textview
            tvDrugName.text = tvDrugName.text.toString()
            tvDrugDesc.text = tvDrugDesc.text.toString()
            tvDrugSide.text = tvDrugSide.text.toString()
            tvDrugTreatment.text = tvDrugTreatment.text.toString()

            alertDialog.dismiss()
        }
    }

    private fun updateDrugData(
        id: String,
        name: String,
        desc: String,
        Side: String,
        treatment: String
    ) {
        val dbRef = FirebaseDatabase.getInstance().getReference("Drugs").child(id)
        val empInfo = DrugModel(id, name, desc, Side, treatment)
        dbRef.setValue(empInfo)
    }

    private fun deleteRecord (
        id: String
    ){
        val dbRef = FirebaseDatabase.getInstance().getReference("Drugs").child(id)
        val mTask = dbRef.removeValue()

        mTask.addOnSuccessListener {
            Toast.makeText(this, "Drug data deleted", Toast.LENGTH_LONG).show()

            val intent = Intent(this, FetchingActivity::class.java)
            finish()
            startActivity(intent)
        }.addOnFailureListener{ error ->
            Toast.makeText(this, "Deleting Err ${error.message}", Toast.LENGTH_LONG).show()
        }
    }
}