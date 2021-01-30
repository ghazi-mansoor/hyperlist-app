package com.example.hyperlist.viewhandlers

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hyperlist.R
import com.example.hyperlist.datahandlers.Task
import org.w3c.dom.Text

class TaskListAdapter(private val clickListener: TaskClickListener) : RecyclerView.Adapter<TaskListAdapter.TaskViewHolder>() {

    private var tasks = emptyList<Task>()

    interface TaskClickListener {
        fun displayPomodoro()
    }

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var taskTitle = itemView.findViewById<TextView>(R.id.taskTitleTextView)
        var startPomodoro = itemView.findViewById<ImageView>(R.id.startPomodoroIcon)
        var taskComplete = itemView.findViewById<ImageView>(R.id.taskCompleteStateIcon)
        var taskTime = itemView.findViewById<TextView>(R.id.taskTimeTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_view_holder, parent, false)
        return TaskViewHolder(view)
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.taskTitle.text = tasks[position].title
        holder.taskTime.text = tasks[position].timeString

        if (tasks[position].complete) {
            holder.taskTitle.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            holder.taskComplete.setImageResource(R.drawable.ic_check_circle_green_24dp)
        }

        else if (!tasks[position].complete) {
            holder.taskTitle.paintFlags =  holder.taskTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            holder.taskComplete.setImageResource(R.drawable.ic_highlight_off_grey_24dp)
        }

        if (tasks[position].important) {
            holder.startPomodoro.setImageResource(R.drawable.ic_play_circle_outline_primary_light_24dp)
        }
        else {
            holder.startPomodoro.setImageResource(R.drawable.ic_play_circle_outline_grey_24dp)
        }

        /*holder.taskTitle.setOnClickListener {
            clickListener.displayPomodoro()
        }*/
        holder.startPomodoro.setOnClickListener {
            clickListener.displayPomodoro()
        }

    }

    internal fun setTasks(tasks: List<Task>) {
        this.tasks = tasks
        notifyDataSetChanged()
    }

    fun getTaskAtPosition(position: Int): Task {
        return tasks[position]
    }

}