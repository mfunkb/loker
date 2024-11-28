package com.marshal.pjbl

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase

class LamarPekerjaan : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_lamar_pekerjaan)

        // Inisialisasi view
        val etNamaLengkap = findViewById<EditText>(R.id.etNamaLengkap)
        val radioGroupGender = findViewById<RadioGroup>(R.id.radioGroupGender)
        val etUmur = findViewById<EditText>(R.id.etUmur)
        val etTelepon = findViewById<EditText>(R.id.etTelepon)
        val etAlamat = findViewById<EditText>(R.id.etAlamat)
        val btnSubmit = findViewById<Button>(R.id.btnSubmit)
        val btnHubigd = findViewById<Button>(R.id.btnadmin) // Tombol Hubungi Admin

        // Firebase Realtime Database instance
        val database =
            FirebaseDatabase.getInstance("https://pjbl-loker-19-default-rtdb.asia-southeast1.firebasedatabase.app/")
        val userRef = database.getReference("Lamaran")

        // Set OnClickListener untuk tombol submit
        btnSubmit.setOnClickListener {
            val namaLengkap = etNamaLengkap.text.toString()
            val selectedGenderId = radioGroupGender.checkedRadioButtonId
            val jenisKelamin = findViewById<RadioButton>(selectedGenderId)?.text.toString()
            val umur = etUmur.text.toString().toIntOrNull()
            val telepon = etTelepon.text.toString()
            val alamat = etAlamat.text.toString()

            if (namaLengkap.isEmpty() || jenisKelamin.isEmpty() || umur == null || telepon.isEmpty() || alamat.isEmpty()) {
                Toast.makeText(this, "Harap isi semua kolom dengan benar", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            // Simpan ke Realtime Database
            val newUserRef = userRef.push() // generate unique ID
            val modeldata = mapOf(
                "nama" to namaLengkap,
                "jenisKelamin" to jenisKelamin,
                "umur" to umur,
                "telepon" to telepon,
                "alamat" to alamat
            )
            newUserRef.setValue(modeldata)
                .addOnSuccessListener {
                    Toast.makeText(this, "Data berhasil disimpan Silahkan Chat Admin", Toast.LENGTH_SHORT).show()
                    finish() // Tutup form
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show()
                }
        }

        // Set OnClickListener untuk tombol Hubungi Admin
        btnHubigd.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://wa.me/6281228291117") // Link WhatsApp yang benar
            startActivity(intent)
        }
    }
}
