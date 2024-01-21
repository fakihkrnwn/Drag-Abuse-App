package com.example.drugabuseapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.drugabuseapp.R
import com.example.drugabuseapp.adapters.EmpAdapter
import com.example.drugabuseapp.models.DrugModel
import com.google.firebase.database.*


class FetchingActivity : AppCompatActivity() {

    private lateinit var empRecyclerView: RecyclerView
    private lateinit var tvLoadingData: TextView
    private lateinit var drugList: ArrayList<DrugModel>
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fetching)

        empRecyclerView = findViewById(R.id.rvDrugs)
        empRecyclerView.layoutManager = LinearLayoutManager(this)
        empRecyclerView.setHasFixedSize(true)
        tvLoadingData = findViewById(R.id.tvLoadingData)

        drugList = arrayListOf<DrugModel>()

        getDrugsData()

    }

    private fun getDrugsData() {

        empRecyclerView.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("Drugs")

        dbRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                drugList.clear()
                if (snapshot.exists()){
                    for (empSnap in snapshot.children){
                        val empData = empSnap.getValue(DrugModel::class.java)
                        drugList.add(empData!!)
                    }
                    val mAdapter = EmpAdapter(drugList)
                    empRecyclerView.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object : EmpAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {

                            val intent = Intent(this@FetchingActivity, DrugDetailsActivity::class.java)

                            //put extras
                            intent.putExtra("drugId", drugList[position].drugId)
                            intent.putExtra("drugName", drugList[position].drugName)
                            intent.putExtra("drugDesc", drugList[position].drugDesc)
                            intent.putExtra("drugSide", drugList[position].drugSide)
                            intent.putExtra("drugTreatment", drugList[position].drugTreatment)
                            startActivity(intent)
                        }

                    })

                    empRecyclerView.visibility = View.VISIBLE
                    tvLoadingData.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}