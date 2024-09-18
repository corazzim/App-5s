package com.example.a5s

import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText

class acao_melhorias : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: Task_Adapter
    private val tasks = mutableListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_acao_melhorias)


                val btnVoltar: ImageView = findViewById(R.id.btn_voltar_melhorias)
                val btnAdd: ImageView = findViewById(R.id.btn_add)

                // Configuração da RecyclerView
                recyclerView = findViewById(R.id.recycler_view_tasks)
                adapter = Task_Adapter(tasks, ::editTask, ::deleteTask)
                recyclerView.adapter = adapter
                recyclerView.layoutManager = LinearLayoutManager(this)

                // Ação do botão Voltar
                btnVoltar.setOnClickListener {
                    finish()
                }

                // Ação do botão Adicionar
                btnAdd.setOnClickListener {
                    showAddTaskDialog()
                }
            }

            private fun showAddTaskDialog() {
                val dialog = Dialog(this)
                dialog.setContentView(R.layout.dailog_add)

                val titleInput: TextInputEditText = dialog.findViewById(R.id.input_Title_tarefa)
                val descriptionInput: TextInputEditText = dialog.findViewById(R.id.inputDescription)
                val btnCreate: Button = dialog.findViewById(R.id.btn_create)

                btnCreate.setOnClickListener {
                    val title = titleInput.text.toString()
                    val description = descriptionInput.text.toString()

                    if (title.isNotBlank() && description.isNotBlank()) {
                        addTask("$title: $description")
                        dialog.dismiss()
                    } else {
                        Toast.makeText(this, "Título e descrição são obrigatórios", Toast.LENGTH_SHORT).show()
                    }
                }

                dialog.show()
            }

            private fun addTask(task: String) {
                tasks.add(task)
                adapter.notifyItemInserted(tasks.size - 1)
                recyclerView.scrollToPosition(tasks.size - 1)
            }

            private fun editTask(position: Int) {
                // Implemente a lógica para editar a tarefa
                val task = tasks[position].split(": ")
                if (task.size == 2) {
                    showEditTaskDialog(position, task[0], task[1])
                }
            }

            private fun deleteTask(position: Int) {
                // Implemente a lógica para excluir a tarefa
                tasks.removeAt(position)
                adapter.notifyItemRemoved(position)
            }

            private fun showEditTaskDialog(position: Int, currentTitle: String, currentDescription: String) {
                val dialog = Dialog(this)
                dialog.setContentView(R.layout.dailog_add)

                val titleInput: TextInputEditText = dialog.findViewById(R.id.input_Title_tarefa)
                val descriptionInput: TextInputEditText = dialog.findViewById(R.id.inputDescription)
                val btnCreate: Button = dialog.findViewById(R.id.btn_create)

                titleInput.setText(currentTitle)
                descriptionInput.setText(currentDescription)
                btnCreate.text = "Atualizar"

                btnCreate.setOnClickListener {
                    val title = titleInput.text.toString()
                    val description = descriptionInput.text.toString()

                    if (title.isNotBlank() && description.isNotBlank()) {
                        tasks[position] = "$title: $description"
                        adapter.notifyItemChanged(position)
                        dialog.dismiss()
                    } else {
                        Toast.makeText(this, "Título e descrição são obrigatórios", Toast.LENGTH_SHORT).show()
                    }
                }

                dialog.show()
            }

}


