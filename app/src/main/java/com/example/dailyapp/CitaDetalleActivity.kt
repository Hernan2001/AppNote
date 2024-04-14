package com.example.dailyapp

import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.SpeechRecognizer
import android.text.Editable
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.view.View
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Timestamp

class CitaDetalleActivity : AppCompatActivity() {
    private lateinit var titleEditText: EditText
    private lateinit var contentEditText: EditText
    private lateinit var guardarBtn: ImageButton
    private lateinit var tituloPag: TextView
    private lateinit var eliminarBtn: TextView
    private var titulo: String? = null
    private var contenido: String? = null
    private var docId: String? = null
    private var isEditMode = false

    private lateinit var microfono: Microfono

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cita_detalle)

        titleEditText = findViewById(R.id.TituloEditTxt)
        contentEditText = findViewById(R.id.ContenidoTxt)
        guardarBtn = findViewById(R.id.GuardarNotaBtn)
        tituloPag = findViewById(R.id.titulo_pag)
        eliminarBtn = findViewById(R.id.EliminarBtn)

        // Recuperar datos
        titulo = intent.getStringExtra("title")
        contenido = intent.getStringExtra("content")
        docId = intent.getStringExtra("docId")

        //cambio titulo pagina
        if (!docId.isNullOrEmpty()) {
            isEditMode = true
        }
        // Establecer los datos en las vistas
        titleEditText.setText(titulo)
        contentEditText.setText(contenido)

        if (isEditMode) {
            tituloPag.text = "Edite su Cita"
            //Se mostrara el boton eliminar cuando se este en modo edición
            eliminarBtn.visibility = View.VISIBLE
        }
        guardarBtn.setOnClickListener { guardarCita() }
        eliminarBtn.setOnClickListener { borrarNotaDeFirebase() }

        microfono = Microfono(this, object : RecognitionListener {
            // Implementa los métodos de la interfaz RecognitionListener aquí
            override fun onReadyForSpeech(params: Bundle?) {}
            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() {}
            override fun onError(error: Int) {}
            override fun onResults(results: Bundle?) {
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (matches != null && matches.isNotEmpty()) {
                    val dictado = matches[0]
                    contentEditText.append("$dictado")
                }
            }

            override fun onPartialResults(partialResults: Bundle?) {
                val matches = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (matches != null && matches.isNotEmpty()) {
                    val partialDictado = matches[0]
                    val editableEnVivo = Editable.Factory.getInstance().newEditable(partialDictado)
                    // Actualizar el contenido del EditText con el dictado
                    contentEditText.text = editableEnVivo
                }
            }
            override fun onEvent(eventType: Int, params: Bundle?) {}
        })
        val startButton = findViewById<ImageButton>(R.id.btnMicro)
        startButton.setOnClickListener {
            Utilidades.showMensaje(this,"Microfono Activado")
            microfono.startSpeechRecognition()
        }
    }
    private fun guardarCita() {
        val noteTitle = titleEditText.text.toString()
        val noteContent = contentEditText.text.toString()

        if (noteTitle.isNullOrEmpty()) {
            Utilidades.showMensaje(this,"El titulo es requerido")
            return
        }
        if (noteContent.isNullOrEmpty()){
            Utilidades.showMensaje(this,"No hay contenido")
            return
        }
        val note = Cita()
        note.title = noteTitle
        note.content = noteContent
        note.timestamp = Timestamp.now()

        saveNotaEnFirebase(note)
    }
    private fun saveNotaEnFirebase(note: Cita) {
        val documentReference: DocumentReference = if (isEditMode) {
            Utilidades.getCollectionReferenceForNotes().document(docId!!)
        } else {
            Utilidades.getCollectionReferenceForNotes().document()
        }
        documentReference.set(note).addOnCompleteListener { task: Task<Void?> ->
            if (task.isSuccessful) {
                //cita agregada
                Utilidades.showMensaje(this@CitaDetalleActivity, "Cita agendada Exitosamente")
                finish()
            } else {
                Utilidades.showMensaje(this@CitaDetalleActivity, "Fallo al guardar la Cita Gil")
            }
        }
    }

    private fun borrarNotaDeFirebase() {
        val documentReference = Utilidades.getCollectionReferenceForNotes().document(docId!!)
        documentReference.delete().addOnCompleteListener { task: Task<Void?> ->
            if (task.isSuccessful) {
                //Eliminar
                Utilidades.showMensaje(this@CitaDetalleActivity, "Nota.kt Eliminada Exitosamente")
                finish()
            } else {
                Utilidades.showMensaje(this@CitaDetalleActivity, "Fallo al Eliminar la Nota.kt")
            }
        }
    }
}