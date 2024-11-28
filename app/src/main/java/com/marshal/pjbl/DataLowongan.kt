package com.marshal.pjbl

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DataLowongan : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AdapterLowongan
    val database = FirebaseDatabase.getInstance("https://pjbl-loker-19-default-rtdb.asia-southeast1.firebasedatabase.app/")
    val reference = database.getReference("LowonganPekerjaan")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_data_lowongan)

        recyclerView = findViewById(R.id.cardlowongan)

        // Set LinearLayoutManager untuk RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = AdapterLowongan(this, ArrayList(), database)
        recyclerView.adapter = adapter



        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val lowonganList = ArrayList<ModelLowongan>()
                for (snapshot in dataSnapshot.children) {
                    val lowongan = snapshot.getValue(ModelLowongan::class.java)
                    lowongan?.let { lowonganList.add(it) }
                }
                adapter.setData(lowonganList)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
            }
        })

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}