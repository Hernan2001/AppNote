package com.example.dailyapp
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import android.content.Context

class CitaAdapter(options: FirestoreRecyclerOptions<Cita>, private val context: Context) :
    FirestoreRecyclerAdapter<Cita, CitaAdapter.NotaViewHolder>(options){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cita_item, parent, false)
        return NotaViewHolder(view)
    }
    override fun onBindViewHolder(holder: NotaViewHolder, position: Int, model: Cita) {
        holder.tituloTextView.text = model.title
        holder.contenidoTextView.text = model.content
        holder.timestampTextView.text = Utilidades.timestampToString(model.timestamp!!)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, CitaDetalleActivity::class.java)
            intent.putExtra("title", model.title)
            intent.putExtra("content", model.content)
            val docId = snapshots.getSnapshot(position).id
            intent.putExtra("docId", docId)
            context.startActivity(intent)
        }
    }
    inner class NotaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tituloTextView: TextView = itemView.findViewById(R.id.nota_titulo_text_view)
        val contenidoTextView: TextView = itemView.findViewById(R.id.nota_contenido_text_view)
        val timestampTextView: TextView = itemView.findViewById(R.id.timestamp_text_view)
    }
}
