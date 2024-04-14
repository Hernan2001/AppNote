package com.example.dailyapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class InicioActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)

        val user = Firebase.auth.currentUser
        if (user == null) {
            startActivity(Intent(this, MainActivity:: class.java))
            finish()
        }

        val btnNot = findViewById<Button>(R.id.btnNotas);
        btnNot.setOnClickListener{
            startActivity(Intent(this, ListaCitasActivity:: class.java))
        }
        //CERRAR SESION
        val logout = findViewById<Button>(R.id.cerrarBtn)
        logout.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        val btnLapiz = findViewById<ImageView>(R.id.lapizBtn)
        btnLapiz.setOnClickListener{
            startActivity(Intent(this,CitaDetalleActivity:: class.java))
        }
    }
}