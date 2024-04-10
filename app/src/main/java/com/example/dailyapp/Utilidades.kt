package com.example.dailyapp

import android.content.Context
import android.widget.Toast
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

import java.text.SimpleDateFormat

class Utilidades {
    companion object {

        fun showMensaje(context: Context, message: String) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }

        fun getCollectionReferenceForNotes(): CollectionReference {
            val currentUser = FirebaseAuth.getInstance().currentUser
            requireNotNull(currentUser) { "Usuario no autenticado" }
            return FirebaseFirestore.getInstance().collection("notas")
                .document(currentUser!!.uid).collection("mis_notas")
        }

        fun timestampToString(timestamp: Timestamp): String {
            return SimpleDateFormat("dd/MM/yyyy").format(timestamp.toDate())
        }
    }
}