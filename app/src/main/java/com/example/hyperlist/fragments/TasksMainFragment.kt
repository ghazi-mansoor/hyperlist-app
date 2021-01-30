package com.example.hyperlist.fragments

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hyperlist.MainActivity

import com.example.hyperlist.R
import com.example.hyperlist.TipsActivity
import com.example.hyperlist.datahandlers.Task
import com.example.hyperlist.helpers.NotificationHelper
import com.example.hyperlist.helpers.ParseTaskUtility
import com.example.hyperlist.helpers.SwipeToDeleteCallback
import com.example.hyperlist.viewhandlers.TaskListAdapter
import com.example.hyperlist.viewhandlers.TaskListViewModel
import kotlinx.android.synthetic.main.fragment_tasks_main.*

/**
 * A simple [Fragment] subclass.
 */
class TasksMainFragment : Fragment(), TaskListAdapter.TaskClickListener {

    private lateinit var mContext: Context
    private lateinit var taskListRecyclerView: RecyclerView
    private lateinit var taskListViewModel: TaskListViewModel
    private lateinit var itemTouchHelper: ItemTouchHelper
    private lateinit var alarmManager: AlarmManager

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tasks_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createNotificationChannel()
        alarmManager = mContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val taskInputTextField = view.findViewById<EditText>(R.id.taskInputTextField)
        val taskImportantCheckbox = view.findViewById<CheckBox>(R.id.taskImportantCheckbox)
        val addTaskButton = view.findViewById<Button>(R.id.addTaskButton)

        taskListRecyclerView = view.findViewById<RecyclerView>(R.id.taskListRecyclerView)
        taskListRecyclerView.layoutManager = LinearLayoutManager(activity)
        val adapter = TaskListAdapter(this)
        taskListRecyclerView.adapter = adapter

        taskListViewModel = ViewModelProvider(this).get(TaskListViewModel::class.java)
        taskListViewModel.allTasks.observe(viewLifecycleOwner, Observer {tasks ->
            tasks?.let { adapter.setTasks(it) }
        })

        val resetButton = view.findViewById<Button>(R.id.resetButton)
        resetButton.setOnClickListener {
            taskListViewModel.resetTasks()
        }

        val swipeToDeleteCallback = object : SwipeToDeleteCallback(mContext, taskListRecyclerView) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                if (direction == ItemTouchHelper.LEFT) {
                    val task = adapter.getTaskAtPosition(viewHolder.adapterPosition)
                    taskListViewModel.deleteTask(task)
                    val hours = task.hours
                    val minutes = task.minutes
                    val taskAlarmID = "$hours$minutes".toInt()

                    NotificationHelper.cancelNotificationTime(taskAlarmID, mContext, alarmManager)
                    Toast.makeText(context, "Task deleted", Toast.LENGTH_SHORT).show()

                }
                else if (direction == ItemTouchHelper.RIGHT) {
                    val task = adapter.getTaskAtPosition(viewHolder.adapterPosition)
                    if (!task.complete) task.complete = true
                    else if (task.complete) task.complete = false
                    taskListViewModel.completeTask(task)
                }
            }
        }

        itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(taskListRecyclerView)

        taskInputTextField.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                taskInputTextFieldLayout.hint = "Enter task"
            }
        }

        addTaskButton.setOnClickListener {
            val taskTitleEntered: String = taskInputTextField.text.toString()
            val taskImportance = taskImportantCheckbox.isChecked

            if (taskTitleEntered.isNotEmpty()) {
                val inputManager = mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0)
                taskInputTextField.setText("")
                taskImportantCheckbox.isChecked = false

                val taskTitleExtracted = ParseTaskUtility.extractTitleTimeStrings(taskTitleEntered)[0]

                val taskTimeString = ParseTaskUtility.extractTitleTimeStrings(taskTitleEntered)[1]

                var taskTimeHours = 0
                var taskTimeMinutes = 0
                var taskTimeMeridiem = ""
                var taskAlarmID: Int = 0

                if (taskTimeString.isNotEmpty()) {
                    taskTimeHours = ParseTaskUtility.extractTimeStrings(taskTimeString)[0].toInt()
                    taskTimeMinutes = ParseTaskUtility.extractTimeStrings(taskTimeString)[1].toInt()
                    taskTimeMeridiem = ParseTaskUtility.extractTimeStrings(taskTimeString)[2]

                    taskAlarmID = "$taskTimeHours$taskTimeMinutes".toInt()
                    NotificationHelper.setNotificationTime(taskTimeHours, taskTimeMinutes, taskTimeMeridiem, taskAlarmID, mContext, alarmManager)
                }

                val task = Task(title = taskTitleExtracted, hours = taskTimeHours, minutes = taskTimeMinutes, meridiem = taskTimeMeridiem, important = taskImportance, timeString = taskTimeString, timeInt = taskAlarmID)
                taskListViewModel.insert(task)
                Toast.makeText(context, "New task added", Toast.LENGTH_SHORT).show()
            }
        }

        val adviceCardView = view.findViewById<CardView>(R.id.adviceCardView)
        adviceCardView.setOnClickListener {
            val intent = Intent(mContext, TipsActivity::class.java)
            startActivity(intent)
        }

    }

    override fun displayPomodoro() {
        findNavController().navigate(R.id.timerMainFragment)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "simpleNotificationChannel"
            val descriptionText = "This channel is used to generate a simple notification"
            val importance =  NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("notChannel02", name, importance).apply {
                description = descriptionText
            }
            val notificationManager : NotificationManager = mContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

}
