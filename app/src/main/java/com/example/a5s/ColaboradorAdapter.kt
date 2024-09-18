package com.example.a5s

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView



class ColaboradorAdapter(private val colaboradores: List<Colaborador>) :
    RecyclerView.Adapter<ColaboradorAdapter.ColaboradorViewHolder>() {

    private val selectedColaboradores = mutableSetOf<Colaborador>()

    inner class ColaboradorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val checkBoxSelect: CheckBox = itemView.findViewById(R.id.checkBoxSelect)
        val textViewNome: TextView = itemView.findViewById(R.id.txtName)
        val textViewMatricula: TextView = itemView.findViewById(R.id.txtmatricula)
        val textViewCargo: TextView = itemView.findViewById(R.id.txtCargofuncao)

        init {
            checkBoxSelect.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    selectedColaboradores.add(colaboradores[adapterPosition])
                } else {
                    selectedColaboradores.remove(colaboradores[adapterPosition])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColaboradorViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_colaborador, parent, false)
        return ColaboradorViewHolder(view)
    }

    override fun onBindViewHolder(holder: ColaboradorViewHolder, position: Int) {
        val colaborador = colaboradores[position]
        holder.textViewNome.text = colaborador.nome
        holder.textViewMatricula.text = colaborador.matricula.toString()
        holder.textViewCargo.text = colaborador.cargo
        holder.checkBoxSelect.isChecked = selectedColaboradores.contains(colaborador)
    }

    override fun getItemCount(): Int = colaboradores.size

    fun getSelectedColaboradores(): Set<Colaborador> = selectedColaboradores
}
