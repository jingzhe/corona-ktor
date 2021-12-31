package com.jingzhe.corona.utils

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

object CoronaUtils {
    fun getTodayDate(): String {
        return getTime("yyyy-MM-dd")
    }

    fun getNextDate(dateString: String): String {
        val date = LocalDate.parse(dateString)
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return date.plusDays(1).format(formatter)
    }

    private fun getTime(pattern: String): String {
        val simpleDateFormat = SimpleDateFormat(pattern)
        simpleDateFormat.timeZone = TimeZone.getTimeZone("Europe/Helsinki")
        val date = Date()
        return simpleDateFormat.format(Date())
    }
}