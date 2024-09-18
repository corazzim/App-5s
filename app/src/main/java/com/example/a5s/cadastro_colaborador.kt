package com.example.a5s

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class cadastro_colaborador : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private var gestorId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_colaborador)

        dbHelper = DatabaseHelper(this)

        // Recuperar o ID do gestor a partir da Intent
        gestorId = intent.getIntExtra("gestor_id", -1)

        if (gestorId == -1) {
            Toast.makeText(this, "ID do gestor não encontrado!", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val nomeEditText: EditText = findViewById(R.id.txtname)
        val cargoEditText: EditText = findViewById(R.id.txtcargo)
        val maquinaEditText: EditText = findViewById(R.id.txtmaquina)
        val matriculaEditText: EditText = findViewById(R.id.editTextMatricula)
        val senhaEditText: EditText = findViewById(R.id.txtsenha)
        val cadastrarButton: Button = findViewById(R.id.btn_cadastrar)

        cadastrarButton.setOnClickListener {
            val nome = nomeEditText.text.toString()
            val cargo = cargoEditText.text.toString()
            val maquina = maquinaEditText.text.toString()

            // Conversão de matricula e senha para Int
            val matricula = matriculaEditText.text.toString().toIntOrNull()
            val senha = senhaEditText.text.toString().toIntOrNull()

            if (nome.isNotEmpty() && cargo.isNotEmpty() && maquina.isNotEmpty() && matricula != null && senha != null) {
                val isSuccess = dbHelper.cadastrarColaborador(gestorId, nome, cargo, maquina, matricula, senha)
                if (isSuccess) {
                    Toast.makeText(this, "Colaborador cadastrado com sucesso!", Toast.LENGTH_SHORT).show()
                    // Voltar para a tela do gestor após o cadastro
                    finish() // Fecha a tela de cadastro e retorna à tela anterior (Gestor_Main)
                } else {
                    Toast.makeText(this, "Erro ao cadastrar colaborador!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Por favor, preencha todos os campos corretamente.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
