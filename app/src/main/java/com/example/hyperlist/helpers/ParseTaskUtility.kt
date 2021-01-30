package com.example.hyperlist.helpers

import android.util.Log
import java.util.regex.Pattern

object ParseTaskUtility {

    private val firstPattern = Pattern.compile("\\b((?:0?[1-9]|1[0-2])(?!\\d| (?![ap]))[:.]?(?:(?:[0-5][0-9]))?(?:\\s?[apAP][mM]))\\b")
    private val secondPattern = Pattern.compile("[apAP][mM]")
    private val thirdPattern = Pattern.compile("\\b((?:0?[1-9]|1[0-2])(?!\\d| (?![ap]))[:.]?(?:(?:[0-5][0-9]))?)\\b")

    private lateinit var taskTitleExtracted: String
    private lateinit var taskTimeExtracted: String

    private lateinit var taskTimeHours: String
    private lateinit var taskTimeMinutes: String
    private lateinit var taskTimeMeridiem: String

    fun extractTitleTimeStrings(taskTitleEntered: String): ArrayList<String> {
        val matched = firstPattern.matcher(taskTitleEntered)

        if (!matched.find()) {
            taskTitleExtracted = taskTitleEntered
            taskTimeExtracted = ""
        }

        else {
            val splittedTaskTitleEntered = taskTitleEntered.split(firstPattern)
            taskTimeExtracted = matched.group()
            taskTitleExtracted = if (splittedTaskTitleEntered[0].contains("AM", true) || splittedTaskTitleEntered[0].contains("PM", true)) {
                splittedTaskTitleEntered[1]
            } else splittedTaskTitleEntered[0]
        }

        return arrayListOf(taskTitleExtracted, taskTimeExtracted)
    }

    fun extractTimeStrings(taskTimeString: String): ArrayList<String> {
        val matchedMeridiemOnly = secondPattern.matcher(taskTimeString)
        taskTimeMeridiem = if (matchedMeridiemOnly.find()) {
            matchedMeridiemOnly.group()
        } else {
            ""
        }

        val matchedTimeOnly = thirdPattern.matcher(taskTimeString)
        val taskTimeOnly = if (matchedTimeOnly.find()) {
            matchedTimeOnly.group()
        } else {
            ""
        }
        taskTimeHours = taskTimeOnly.split(":")[0]
        taskTimeMinutes = taskTimeOnly.split(":")[1]

        var formattedTaskTimeHours: String = taskTimeHours
        var formattedTaskTimeMinutes: String = taskTimeMinutes

        if (taskTimeHours.startsWith("0", true)) {
            formattedTaskTimeHours = taskTimeHours.substring(1)
        }

        if (taskTimeMinutes.startsWith("0", true)) {
            formattedTaskTimeMinutes = taskTimeMinutes.substring(1)
        }

        return arrayListOf(formattedTaskTimeHours, formattedTaskTimeMinutes, taskTimeMeridiem)
    }

}