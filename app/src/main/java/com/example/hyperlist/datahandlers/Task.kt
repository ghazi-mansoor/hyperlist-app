package com.example.hyperlist.datahandlers

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task_table")
data class Task(@PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id: Int = 0, @ColumnInfo(name = "title") var title: String, @ColumnInfo(name = "hours") var hours: Int,
                @ColumnInfo(name = "minutes") var minutes: Int, @ColumnInfo(name = "meridiem") var meridiem: String,
                @ColumnInfo(name = "complete") var complete: Boolean = false, @ColumnInfo(name = "important")
                var important: Boolean = false, @ColumnInfo(name = "timeString") var timeString: String,  @ColumnInfo(name = "timeInt") var timeInt: Int)