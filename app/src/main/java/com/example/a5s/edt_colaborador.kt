package com.example.a5s

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class edt_colaborador : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ColaboradorAdapter
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edt_colaborador)

        dbHelper = DatabaseHelper(this)

        recyclerView = findViewById(R.id.recyclerViewColaboradores)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val colaboradores = dbHelper.getColaboradores()
        adapter = ColaboradorAdapter(colaboradores)
        recyclerView.adapter = adapter

        val checkBoxSelectAll: CheckBox = findViewById(R.id.checkBoxSelectAll)
        val btnExcluirSelecionados: Button = findViewById(R.id.btnExcluirSelecionados)

        checkBoxSelectAll.setOnCheckedChangeListener { _, isChecked ->
            for (i in 0 until recyclerView.childCount) {
                val viewHolder = recyclerView.findViewHolderForAdapterPosition(i) as? ColaboradorAdapter.ColaboradorViewHolder
                viewHolder?.checkBoxSelect?.isChecked = isChecked
            }
        }

        btnExcluirSelecionados.setOnClickListener {
            val selectedColaboradores = adapter.getSelectedColaboradores()
            if (selectedColaboradores.isNotEmpty()) {
                selectedColaboradores.forEach { colaborador ->
                    dbHelper.deleteColaborador(colaborador.matricula)
                }
                // Atualizar a lista após exclusão
                val updatedColaboradores = dbHelper.getColaboradores()
                adapter = ColaboradorAdapter(updatedColaboradores)
                recyclerView.adapter = adapter
                Toast.makeText(this, "Colaboradores excluídos.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Nenhum colaborador selecionado.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
