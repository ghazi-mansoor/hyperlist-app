package com.example.hyperlist.viewhandlers

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.hyperlist.datahandlers.HyperListDatabase
import com.example.hyperlist.datahandlers.Task
import com.example.hyperlist.datahandlers.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskListViewModel(application: Application) : AndroidViewModel(application) {

    private val taskRepository: TaskRepository

    val allTasks: LiveData<List<Task>>

    init {
        val taskDao = HyperListDatabase.getDatabase(application, viewModelScope).taskDao()
        taskRepository = TaskRepository(taskDao)
        allTasks = taskRepository.allTasks
    }

    fun insert(task: Task) = viewModelScope.launch(Dispatchers.IO) {
        taskRepository.insert(task)
    }

    fun deleteTask(task: Task) = viewModelScope.launch(Dispatchers.IO) {
        taskRepository.deleteTask(task)
    }

    fun completeTask(task: Task) = viewModelScope.launch(Dispatchers.IO) {
        taskRepository.completeTask(task)
    }

    fun resetTasks() = viewModelScope.launch(Dispatchers.IO) {
        taskRepository.resetTasks()
    }

}