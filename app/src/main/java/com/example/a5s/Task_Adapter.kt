package com.example.a5s

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView

class Task_Adapter(
    private val tasks: MutableList<String>,
    private val onEdit: (Int) -> Unit,
    private val onDelete: (Int) -> Unit
) : RecyclerView.Adapter<Task_Adapter.TaskViewHolder>() {


            // Set to track which positions are expanded
            private val expandedPositions: MutableSet<Int> = mutableSetOf()

            inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
                val taskTitle: TextView = itemView.findViewById(R.id.task_title)
                val taskDescription: TextView = itemView.findViewById(R.id.task_description)
                val editTask: ImageView = itemView.findViewById(R.id.edit_task)
                val deleteTask: ImageView = itemView.findViewById(R.id.delete_task)
            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)
                return TaskViewHolder(view)
            }

            override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
                val task = tasks[position].split(": ")
                if (task.size == 2) {
                    holder.taskTitle.text = task[0]
                    holder.taskDescription.text = task[1]
                }

                // Toggle visibility of task description based on expansion state
                val isExpanded = expandedPositions.contains(position)
                holder.taskDescription.visibility = if (isExpanded) View.VISIBLE else View.GONE

                // Set click listener on itemView to expand/collapse task description
                holder.itemView.setOnClickListener {
                    if (isExpanded) {
                        expandedPositions.remove(position)
                    } else {
                        expandedPositions.add(position)
                    }
                    notifyItemChanged(position)
                }

                holder.editTask.setOnClickListener {
                    onEdit(position)
                }

                holder.deleteTask.setOnClickListener {
                    onDelete(position)
                }
            }

            override fun getItemCount(): Int = tasks.size


}
