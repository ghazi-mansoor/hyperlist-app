package com.example.hyperlist.datahandlers

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alarm_table")
data class Alarm(@PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id: Int = 0, @ColumnInfo(name = "set") var set: Boolean)