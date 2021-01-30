package com.example.hyperlist.datahandlers

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TaskDAO {

    @Query("SELECT * from task_table ORDER BY meridiem ASC, hours, minutes ASC")
    fun getTasks(): LiveData<List<Task>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(task: Task)

    @Delete
    fun deleteTask(task: Task)

    @Update
    fun completeTask(task: Task)

    @Query("UPDATE task_table SET complete = 0")
    fun resetTasks()

}