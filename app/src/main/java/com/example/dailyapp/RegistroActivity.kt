package com.example.dailyapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class RegistroActivity : AppCompatActivity() {

    val db = Firebase.firestore
    private lateinit var createAccountBtn: Button
    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        // Dentro de onCreate después de setContentView

        createAccountBtn = findViewById(R.id.btnRegistrarse)

        nameEditText = findViewById(R.id.txtName)
        emailEditText = findViewById(R.id.txtEmail)
        passwordEditText = findViewById(R.id.txtContra)



        val btnRe = findViewById<Button>(R.id.btnRegistrarse)
        btnRe.setOnClickListener {
            val name = nameEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (validateFields(name, email, password)) {
                registrarUsuarioFB(email, password, name)
                showMensaje("Registro Completo")
                startActivity(Intent(this, LoginActivity:: class.java))
            }
        }
    }

    private fun validateFields(name: String, email: String, password: String): Boolean {
        // Validar el nombre de usuario
        if (!name.matches(Regex("[A-Z][a-z]*"))) {
            showMensaje("Nombre de usuario inválido. Debe empezar con una letra mayúscula seguida de letras minúsculas.")
            return false
        }
        // Validar el correo electrónico
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showMensaje("Correo electrónico inválido")
            return false
        }
        // Validar la contraseña
        if (password.length < 6 || !password.any { it.isDigit() }) {
            showMensaje("Contraseña inválida (mínimo 6 caracteres con al menos un número)")
            return false
        }
        return true
    }

    private fun registrarUsuarioFB(  email: String, password: String,name:String) {
        CoroutineScope(Dispatchers.Main).launch {

            try {
                val authResult =
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).await()

                authResult.user
                guardarUsuario(authResult.user?.uid, email, name)
                finish()
            } catch (e: FirebaseAuthException) {
                // Manejar errores de autenticación aquí
                showMensaje("Error al registrar el usuario: ${e.message}")
            }
        }
    }

    //Llama al UID de la FireStore
    private fun guardarUsuario(uid: String?, email: String, name: String){
        // Create a new user with a first and last name
        val user = hashMapOf(
            "nombre" to name,
            "email" to email,
            "uid" to uid,
        )
        // Agrega nuevo Registro
        db.collection("users")
            .add(user)
            .addOnSuccessListener { documentReference ->
                //startActivity(Intent(this, LoginActivity:: class.java))
                intent.putExtra("uid",uid);
            }
            .addOnFailureListener { e ->
                showMensaje("Error al registrar el usuario: ${e.message}")
            }
    }
    private fun showMensaje(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
