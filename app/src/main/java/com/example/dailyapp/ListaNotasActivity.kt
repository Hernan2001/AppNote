package com.example.dailyapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore

class ListaNotasActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var notasAdapter: NotaAdapter
    private lateinit var addNoteBtn: FloatingActionButton
    var firestoreDB = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_notas)

        addNoteBtn = findViewById(R.id.addNoteButton)
        recyclerView = findViewById(R.id.recycler_view)
        //recyclerView.layoutManager = LinearLayoutManager(this)
        firestoreDB = FirebaseFirestore.getInstance()

        addNoteBtn.setOnClickListener {
            startActivity(Intent(this, NotaDetalleActivity::class.java))
        }
        confiRecyclerView()
    }
    private fun confiRecyclerView() {
        val query = Utilidades.getCollectionReferenceForNotes().orderBy("timestamp", Query.Direction.DESCENDING)
        val options = FirestoreRecyclerOptions.Builder<Nota>()
            .setQuery(query, Nota::class.java)
            .build()

        recyclerView.layoutManager = LinearLayoutManager(this)
        notasAdapter = NotaAdapter(options, this)
        recyclerView.adapter = notasAdapter
    }

    override fun onStart() {
        super.onStart()
        notasAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        notasAdapter.stopListening()
    }

    override fun onResume() {
        super.onResume()
        notasAdapter.notifyDataSetChanged()
    }
}