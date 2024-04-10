package com.example.dailyapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import com.example.dailyapp.MainActivity;

class LoginActivity : AppCompatActivity() {
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private val auth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        emailInput = findViewById(R.id.txtUsuario)
        passwordInput = findViewById(R.id.txtPassword)
        val btnI = findViewById<Button>(R.id.btnIniciar)

        btnI.setOnClickListener {
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                showMensaje("Faltan Datos")
            } else {
                // Verificar si el usuario ya está registrado antes de intentar iniciar sesión
                verificarExistencia(email, password)

            }
        }
    }
    private fun verificarExistencia(email: String, password: String) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                // Intentar iniciar sesión para verificar si el usuario existe
                auth.signInWithEmailAndPassword(email, password).await()

                //val intent = Intent(this@LoginActivity, InicioActivity::class.java)
                //startActivity(intent)
                // Guarda el estado de la sesión en Firebase Auth
                val auth = Firebase.auth
                val user = auth.currentUser
                if (user != null) {
                    val intent = Intent(this@LoginActivity, InicioActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                //finish()//Cuerra el Login

            } catch (e: FirebaseAuthException) {
                when (e.errorCode) {
                    "ERROR_WRONG_PASSWORD" -> {
                        // Contraseña incorrecta
                        showMensaje("Contraseña incorrecta. Inténtalo nuevamente.")
                    }
                    "ERROR_USER_NOT_FOUND", "ERROR_INVALID_EMAIL" -> {
                        // Usuario no encontrado o email mal escrito
                        showMensaje("Correo electrónico no encontrado, Verifica e intenta nuevamente.")
                    }
                    else -> {
                        // Otros errores de autenticación
                        showMensaje("Error de la Cuenta")
                    }
                }
            }
        }
    }
    private fun showMensaje(message: String) {
        Toast.makeText(this@LoginActivity, message, Toast.LENGTH_SHORT).show()
    }
}
