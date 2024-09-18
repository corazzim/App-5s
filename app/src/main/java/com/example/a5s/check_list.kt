package com.example.a5s

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class check_list : AppCompatActivity() {

    private lateinit var contentLayouts: List<LinearLayout>
    private lateinit var titles: List<TextView>
    private lateinit var sendButton: Button
    private lateinit var maquinaSpinner: Spinner

    private var countSim = 0
    private var countNao = 0

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_check_list)

        sharedPreferences = getSharedPreferences("CHECK_LIST_PREFS", Context.MODE_PRIVATE)

        titles = listOf(
            findViewById(R.id.title_seiri),
            findViewById(R.id.title_seiton),
            findViewById(R.id.title_seiso),
            findViewById(R.id.title_seike),
            findViewById(R.id.title_shitsuke)
        )

        contentLayouts = listOf(
            findViewById(R.id.content_seiri),
            findViewById(R.id.content_seiton1),
            findViewById(R.id.content_seiso1),
            findViewById(R.id.content_seike1),
            findViewById(R.id.content_shitsuke1)
        )

        sendButton = findViewById(R.id.button_send)

        maquinaSpinner = findViewById(R.id.spinner_maquinas)
        val maquinas = (1..10).map { "Máquina $it" }  // Gera a lista de máquinas
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, maquinas)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        maquinaSpinner.adapter = adapter

        titles.forEachIndexed { index, textView ->
            textView.setOnClickListener {
                toggleContentVisibility(index)
            }
        }

        val seiriQuestions = listOf(
            listOf(findViewById<CheckBox>(R.id.check_seiri_task_1_ok), findViewById(R.id.check_seiri_task_1_nok)),
            listOf(findViewById(R.id.check_seiri_task_2_ok), findViewById(R.id.check_seiri_task_2_nok)),
            listOf(findViewById(R.id.check_seiri_task_3_ok), findViewById(R.id.check_seiri_task_3_nok)),
            listOf(findViewById(R.id.check_seiri_task_4_ok), findViewById(R.id.check_seiri_task_4_nok))
        )
        val seitonQuestions = listOf(
            listOf(findViewById<CheckBox>(R.id.check_seiton_task_1_ok), findViewById(R.id.check_seiton_task_1_nok)),
            listOf(findViewById(R.id.check_seiton_task_2_ok), findViewById(R.id.check_seiton_task_2_nok)),
            listOf(findViewById(R.id.check_seiton_task_3_ok), findViewById(R.id.check_seiton_task_3_nok)),
            listOf(findViewById(R.id.check_seiton_task_4_ok), findViewById(R.id.check_seiton_task_4_nok))
        )
        val seisoQuestions = listOf(
            listOf(findViewById<CheckBox>(R.id.check_seiso_task_1_ok), findViewById(R.id.check_seiso_task_1_nok)),
            listOf(findViewById(R.id.check_seiso_task_2_ok), findViewById(R.id.check_seiso_task_2_nok)),
            listOf(findViewById(R.id.check_seiso_task_3_ok), findViewById(R.id.check_seiso_task_3_nok)),
            listOf(findViewById(R.id.check_seiso_task_4_ok), findViewById(R.id.check_seiso_task_4_nok))
        )
        val seikeQuestions = listOf(
            listOf(findViewById<CheckBox>(R.id.check_seike_task_1_ok), findViewById(R.id.check_seike_task_1_nok)),
            listOf(findViewById(R.id.check_seike_task_2_ok), findViewById(R.id.check_seike_task_2_nok)),
            listOf(findViewById(R.id.check_seike_task_3_ok), findViewById(R.id.check_seike_task_3_nok)),
            listOf(findViewById(R.id.check_seike_task_4_ok), findViewById(R.id.check_seike_task_4_nok))
        )
        val shitsukeQuestions = listOf(
            listOf(findViewById<CheckBox>(R.id.check_shitsuke_task_1_ok), findViewById(R.id.check_shitsuke_task_1_nok)),
            listOf(findViewById(R.id.check_shitsuke_task_2_ok), findViewById(R.id.check_shitsuke_task_2_nok)),
            listOf(findViewById(R.id.check_shitsuke_task_3_ok), findViewById(R.id.check_shitsuke_task_3_nok)),
            listOf(findViewById(R.id.check_shitsuke_task_4_ok), findViewById(R.id.check_shitsuke_task_4_nok))
        )

        val allQuestions = seiriQuestions + seitonQuestions + seisoQuestions + seikeQuestions + shitsukeQuestions

        allQuestions.forEach { question ->
            setupExclusiveCheck(question[0], question[1])
        }

        sendButton.setOnClickListener {
            if (areAllQuestionsAnswered(allQuestions)) {
                val selectedMachine = maquinaSpinner.selectedItem
                if (selectedMachine == null || selectedMachine == "Selecione uma máquina") {
                    Toast.makeText(this, "Selecione uma máquina antes de enviar!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                countSim = countResponses(allQuestions, true)
                countNao = countResponses(allQuestions, false)

                val machineNumber = selectedMachine.toString().split(" ").last().toInt()
                with(sharedPreferences.edit()) {
                    putInt("MACHINE_${machineNumber}_SIM", countSim)
                    putInt("MACHINE_${machineNumber}_NAO", countNao)
                    apply()
                }

                Toast.makeText(this, "Formulário enviado com sucesso!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, check_imagem::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Preencha todas as perguntas antes de enviar!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun toggleContentVisibility(index: Int) {
        val contentToToggle = contentLayouts[index]
        if (contentToToggle.visibility == View.VISIBLE) {
            contentToToggle.visibility = View.GONE
        } else {
            contentLayouts.forEach { it.visibility = View.GONE }
            contentToToggle.visibility = View.VISIBLE
        }
    }

    private fun setupExclusiveCheck(checkBox1: CheckBox, checkBox2: CheckBox) {
        checkBox1.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) checkBox2.isChecked = false
        }
        checkBox2.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) checkBox1.isChecked = false
        }
    }

    private fun areAllQuestionsAnswered(questions: List<List<CheckBox>>): Boolean {
        return questions.all { question ->
            question.any { it.isChecked }
        }
    }

    private fun countResponses(questions: List<List<CheckBox>>, isSim: Boolean): Int {
        return questions.count { question ->
            val checkBox = if (isSim) question[0] else question[1]
            checkBox.isChecked
        }
    }
}
