package com.example.hyperlist.datahandlers

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

class TaskRepository(private val taskDao: TaskDAO) {

    val allTasks: LiveData<List<Task>> = taskDao.getTasks()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(task: Task) {
        taskDao.insert(task)
    }
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task)
    }
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun completeTask(task: Task) {
        taskDao.completeTask(task)
    }
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun resetTasks() {
        taskDao.resetTasks()
    }

}