package com.alinaincorporated.diploma.ui.add_transaction

import com.alinaincorporated.diploma.R
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.*

object AddTransactionTimePickerHelper {

    fun createPicker(): MaterialTimePicker {
        val calendar = Calendar.getInstance()

        return MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setTitleText(R.string.title_add_transaction_time_picker)
            .setHour(calendar[Calendar.HOUR])
            .setMinute(calendar[Calendar.MINUTE])
            .build()
    }

    fun withDate(picker: MaterialTimePicker, dateMillis: Long): Long {
        return Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
            timeInMillis = dateMillis
            this[Calendar.HOUR] = picker.hour
            this[Calendar.MINUTE] = picker.minute
        }.timeInMillis
    }
}
