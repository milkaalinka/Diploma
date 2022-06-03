package com.alinaincorporated.diploma.ui.utils.input_filter

import android.text.InputFilter
import android.text.Spanned

class OnlyOneZeroAtStartInputFilter : InputFilter {

    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        val length = dest.length
        if (length > 1) return null

        val lastZeroPosition = source.indexOfFirst { it != '0' }
            .takeIf { it > 0 }
            ?.let { it - 1 }
            ?: return null

        if (lastZeroPosition < 1) return null

        return if (lastZeroPosition == source.length - 1) {
            if (dest.isEmpty()) "0"
            else ""
        } else source.substring(lastZeroPosition + 1, source.length)
    }
}