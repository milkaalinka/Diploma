package com.alinaincorporated.diploma.ui.utils.input_filter

import android.text.InputFilter
import android.text.Spanned
import java.util.regex.Pattern

class DecimalDigitsInputFilter : InputFilter {

    companion object {
        private const val DIGITS_BEFORE_DOT = 9
        private const val DIGITS_AFTER_DOT = 2
    }

    private val pattern = Pattern.compile(
        "[0-9]{0,$DIGITS_BEFORE_DOT}+((\\.[0-9]{0,$DIGITS_AFTER_DOT})?)|(\\.)?"
    )

    override fun filter(
        source: CharSequence?,
        start: Int,
        end: Int,
        dest: Spanned?,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        return pattern.matcher(dest.toString() + source.toString())
            .let {
                if (it.matches()) null
                else ""
            }
    }
}