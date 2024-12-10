package com.inmobixpress.inmobixpress.ui.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

@RequiresApi(Build.VERSION_CODES.O)
fun convertMillisToLocalDate(millis: Long): LocalDate {
    return Instant
        .ofEpochMilli(millis)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
}

@RequiresApi(Build.VERSION_CODES.O)
fun convertMillisToLocalDateWithFormatter(
    date: LocalDate,
    dateTimeFormatter: DateTimeFormatter
): LocalDate {
    //Convert the date to a long in millis using a dateformmater
    val dateInMillis = LocalDate.parse(date.format(dateTimeFormatter), dateTimeFormatter)
        .atStartOfDay(
            ZoneId.systemDefault()
        )
        .toInstant()
        .toEpochMilli()

    //Convert the millis to a localDate object
    return Instant
        .ofEpochMilli(dateInMillis)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
}

@RequiresApi(Build.VERSION_CODES.O)
fun dateToString(date: LocalDate): String {
    val dateFormatter =
        DateTimeFormatter.ofPattern("EEEE, dd MMMM, yyyy", Locale.getDefault())
    val dateInMillis = convertMillisToLocalDateWithFormatter(date, dateFormatter)
    return dateFormatter.format(dateInMillis)
        .replaceFirstChar { it.uppercaseChar() }
}

fun dateToString(millis: Long): String {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        return dateToString(convertMillisToLocalDate(millis))
    } else {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        calendar.timeInMillis = millis
        val formatter = SimpleDateFormat("EEEE, dd MMMM, yyyy", Locale.getDefault())
        return formatter.format(calendar.time)
    }
}

fun validateDayOfWeek(millis: Long): Boolean = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
    val dayOfWeek = Instant
        .ofEpochMilli(millis)
        .atZone(
            ZoneId.systemDefault()
        )
        .toLocalDate()
        .dayOfWeek
    dayOfWeek != DayOfWeek.SUNDAY && convertMillisToLocalDate(millis.plus(hourToMillis(hours = 5))).isAfter(
        convertMillisToLocalDate(System.currentTimeMillis())
    ) || convertMillisToLocalDate(millis.plus(hourToMillis(hours = 5))).dayOfMonth == convertMillisToLocalDate(
        System.currentTimeMillis()
    ).dayOfMonth
} else {
    val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
    calendar.timeInMillis = millis
    calendar[Calendar.DAY_OF_WEEK] != Calendar.SUNDAY && millis.plus(
        other = hourToMillis(5)
    ) >= System.currentTimeMillis().minus(other = hourToMillis(5))
}

fun today(): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        dateToString(
            convertMillisToLocalDate(System.currentTimeMillis())
        )
    } else {
        dateToString(System.currentTimeMillis())
    }
}

fun year(): Int {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        return LocalDate.now(
            ZoneId.systemDefault()
        ).year
    } else {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        calendar.timeInMillis = System.currentTimeMillis()
        return calendar[Calendar.YEAR]
    }
}

fun hourToMillis(hours: Int): Long = 3600000L * hours.toLong()

@RequiresApi(Build.VERSION_CODES.O)
fun Long?.millisToLocalDateTime(): LocalDateTime {
    return Instant.ofEpochMilli(this ?: 0)
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime()
}

// Used in Composable
@RequiresApi(Build.VERSION_CODES.O)
fun Long?.millisToLocalDate(): LocalDate {
    return this.millisToLocalDateTime().toLocalDate()
}