package com.alinaincorporated.diploma.ui.utils

import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ROOT)

private val balanceFormat = DecimalFormat("'$'0.00")

fun formatDateTime(dateTime: Long): String {
    return dateFormat.format(dateTime)
}

fun formatMoney(money: Float): String {
    return balanceFormat.format(money)
}