package com.alinaincorporated.diploma.ui.utils.input_filter

import android.text.InputFilter
import android.text.Spanned

open class ComplexInputFilter(
    filters: List<InputFilter>
) : InputFilter {

    private val filters = filters.toList()

    override fun filter(
        source: CharSequence?,
        start: Int,
        end: Int,
        dest: Spanned?,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        for (filter in filters) {
            val result = filter.filter(source, start, end, dest, dstart, dend)
            if (result != null) return result
        }
        return null
    }
}