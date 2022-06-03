package com.alinaincorporated.diploma.ui.add_transaction

import com.alinaincorporated.diploma.R
import com.google.android.material.datepicker.*
import java.util.*

object AddTransactionDatePickerHelper {

    fun createPicker(): MaterialDatePicker<Long> {
        return MaterialDatePicker.Builder.datePicker()
            .setTitleText(R.string.title_add_transaction_date_picker)
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .setCalendarConstraints(createDatePickerConstraints())
            .build()
    }

    private fun createDatePickerConstraints(): CalendarConstraints {
        val today = MaterialDatePicker.todayInUtcMilliseconds()
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        calendar.timeInMillis = today
        setCalendarAtStartOfDay(calendar)
        val max = calendar.timeInMillis

        calendar.add(Calendar.MONTH, -1)
        val min = calendar.timeInMillis

        val validator = CompositeDateValidator.allOf(
            listOf(
                DateValidatorPointForward.from(min),
                DateValidatorPointBackward.before(max),
            )
        )

        return CalendarConstraints.Builder()
            .setStart(min)
            .setEnd(max)
            .setValidator(validator)
            .build()
    }

    private fun setCalendarAtStartOfDay(calendar: Calendar) {
        calendar[Calendar.MILLISECOND] = 0
        calendar[Calendar.SECOND] = 0
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.HOUR] = 0
    }
}
