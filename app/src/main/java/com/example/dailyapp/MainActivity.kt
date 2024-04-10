package com.example.dailyapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        println("Iniciando Proyeccto")

        val btnR = findViewById<Button>(R.id.btnRegistro);
        btnR.setOnClickListener{
            startActivity(Intent(this, RegistroActivity:: class.java))
            finish()
        }
        val btnI = findViewById<Button>(R.id.btnIngresar)
        btnI.setOnClickListener{
            startActivity(Intent(this, LoginActivity:: class.java))
            finish()
        }
    }
}