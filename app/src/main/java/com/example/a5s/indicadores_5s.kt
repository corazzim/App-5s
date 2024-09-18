package com.example.a5s

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter

class indicadores_5s : AppCompatActivity() {

    private lateinit var barChart: BarChart
    private lateinit var spinnerMaquinas: Spinner
    private lateinit var btnVoltarInd: ImageView
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_indicadores5s)

        barChart = findViewById(R.id.barChart)
        spinnerMaquinas = findViewById(R.id.spinner_maquinas_ind)
        btnVoltarInd = findViewById(R.id.btn_voltar_ind)
        sharedPreferences = getSharedPreferences("CHECK_LIST_PREFS", Context.MODE_PRIVATE)

        val maquinas = (1..10).map { "Máquina $it" } + "Todas as Máquinas"
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, maquinas)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerMaquinas.adapter = adapter

        spinnerMaquinas.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedMachine = parent.getItemAtPosition(position).toString()
                displayChart(selectedMachine)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        btnVoltarInd.setOnClickListener {
            finish()
        }
    }

    private fun displayChart(machine: String) {
        val totalPerguntas = 20
        val countSim: Int
        val countNao: Int

        if (machine == "Todas as Máquinas") {
            val totalSim = (1..10).sumOf { getTotalSimForMachine(it) }
            val totalNao = (1..10).sumOf { getTotalNaoForMachine(it) }
            val numMachines = 10
            countSim = if (numMachines > 0) (totalSim * 100) / (numMachines * totalPerguntas) else 0
            countNao = if (numMachines > 0) (totalNao * 100) / (numMachines * totalPerguntas) else 0
        } else {
            val machineNumber = machine.split(" ").last().toIntOrNull() ?: 0
            countSim = (getTotalSimForMachine(machineNumber) * 100) / totalPerguntas
            countNao = (getTotalNaoForMachine(machineNumber) * 100) / totalPerguntas
        }

        val entries = arrayListOf(
            BarEntry(0f, countSim.toFloat()),
            BarEntry(1f, countNao.toFloat())
        )
        val dataSet = BarDataSet(entries, "Respostas")
        dataSet.colors = listOf(
            ContextCompat.getColor(this, R.color.green),
            ContextCompat.getColor(this, R.color.red)
        )
        dataSet.valueTextSize = 12f
        dataSet.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return "${value.toInt()}%"
            }
        }
        val barData = BarData(dataSet)

        barChart.data = barData
        barChart.xAxis.valueFormatter = IndexAxisValueFormatter(listOf("Sim", "Não"))
        barChart.xAxis.granularity = 1f
        barChart.xAxis.labelCount = 2
        barChart.axisLeft.axisMinimum = 0f
        barChart.axisLeft.axisMaximum = 100f
        barChart.axisLeft.granularity = 10f
        barChart.axisLeft.setDrawGridLines(false)
        barChart.axisRight.isEnabled = false
        barChart.description.isEnabled = false

        // Add value text position below the bars
        barChart.setDrawValueAboveBar(true)

        barChart.invalidate()
    }

    private fun getTotalSimForMachine(machineNumber: Int): Int {
        return sharedPreferences.getInt("MACHINE_${machineNumber}_SIM", 0)
    }

    private fun getTotalNaoForMachine(machineNumber: Int): Int {
        return sharedPreferences.getInt("MACHINE_${machineNumber}_NAO", 0)
    }
}
