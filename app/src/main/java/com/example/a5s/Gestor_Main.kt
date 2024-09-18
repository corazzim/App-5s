package com.example.a5s

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class Gestor_Main : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_gestor_main)

        val cadastroColab: ImageView = findViewById(R.id.cadastro_colab)
        val indDesempenho: ImageView = findViewById(R.id.desempenho)
        val edtColab: ImageView = findViewById(R.id.edt_colab)
        val melhorias: ImageView = findViewById(R.id.melhorias)
        val btnSair: Button = findViewById(R.id.btn_sair_gestor)

        // Recuperar o ID do gestor a partir do Intent
        val gestorId = intent.getIntExtra("GESTOR_ID", -1)

        cadastroColab.setOnClickListener {
            if (gestorId != -1) {
                val intent = Intent(this, cadastro_colaborador::class.java).apply {
                    putExtra("gestor_id", gestorId)
                }
                startActivity(intent)
            } else {
                Toast.makeText(this, "Gestor não encontrado", Toast.LENGTH_SHORT).show()
            }
        }

        indDesempenho.setOnClickListener {
            val intent = Intent(this, indicadores_5s::class.java)
            startActivity(intent)
        }

        edtColab.setOnClickListener {
            val intent = Intent(this, edt_colaborador::class.java)
            startActivity(intent)
        }

        melhorias.setOnClickListener {
            val intent = Intent(this, acao_melhorias::class.java)
            startActivity(intent)
        }

        btnSair.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
