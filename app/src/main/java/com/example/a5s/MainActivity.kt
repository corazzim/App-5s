package com.example.a5s

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = DatabaseHelper(this)

        val loginButton: Button = findViewById(R.id.loginBtn)
        val gestorLogin: TextView = findViewById(R.id.gestor_login)
        val matriculaEditText: EditText = findViewById(R.id.matricula_Edit_Text)
        val senhaEditText: EditText = findViewById(R.id.password_Edit_Text)

        loginButton.setOnClickListener {
            val matriculaStr = matriculaEditText.text.toString()
            val senhaStr = senhaEditText.text.toString()

            // Converter matrícula e senha para Int
            val matricula = matriculaStr.toIntOrNull()
            val senha = senhaStr.toIntOrNull()

            if (matricula != null && senha != null) {
                // Primeiro, verificar se é um gestor
                if (dbHelper.verificarLoginGestor(matricula, senha)) {
                    val gestorId = dbHelper.getGestorIdByMatricula(matricula)
                    if (gestorId != -1) {
                        val intent = Intent(this, Gestor_Main::class.java)
                        intent.putExtra("GESTOR_ID", gestorId)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "ID do gestor não encontrado!", Toast.LENGTH_SHORT).show()
                    }
                }
                // Se não for um gestor, verificar se é um colaborador
                else if (dbHelper.verificarLoginColaborador(matricula, senha)) {
                    val intent = Intent(this, check_list::class.java) // Corrigir o nome da classe para CheckList
                    intent.putExtra("COLABORADOR_MATRICULA", matricula)
                    startActivity(intent)
                }
                // Se nenhum login for válido
                else {
                    Toast.makeText(this, "Credenciais inválidas!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Por favor, insira uma matrícula e senha válidas.", Toast.LENGTH_SHORT).show()
            }
        }

        gestorLogin.setOnClickListener {
            showGestorLoginDialog()
        }
    }

    private fun showGestorLoginDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dailog_login, null)
        val dialogBuilder = AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle("Login do Gestor")

        val alertDialog = dialogBuilder.create()
        alertDialog.show()

        val editTextMatricula = dialogView.findViewById<EditText>(R.id.editTextMatricula)
        val editTextSenha = dialogView.findViewById<EditText>(R.id.editTextSenha)
        val buttonEntrar = dialogView.findViewById<Button>(R.id.buttonEntrar)

        buttonEntrar.setOnClickListener {
            val matriculaStr = editTextMatricula.text.toString()
            val senhaStr = editTextSenha.text.toString()

            // Converter matrícula e senha para Int
            val matricula = matriculaStr.toIntOrNull()
            val senha = senhaStr.toIntOrNull()

            if (matricula != null && senha != null) {
                if (dbHelper.verificarLoginGestor(matricula, senha)) {
                    val gestorId = dbHelper.getGestorIdByMatricula(matricula)
                    if (gestorId != -1) {
                        val intent = Intent(this, Gestor_Main::class.java)
                        intent.putExtra("GESTOR_ID", gestorId)
                        startActivity(intent)
                        alertDialog.dismiss()
                    } else {
                        Toast.makeText(this, "ID do gestor não encontrado!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Login de gestor inválido!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Por favor, insira uma matrícula e senha válidas.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
