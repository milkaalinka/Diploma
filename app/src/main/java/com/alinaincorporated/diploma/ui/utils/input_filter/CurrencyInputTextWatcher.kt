package com.alinaincorporated.diploma.ui.utils.input_filter

import android.text.TextWatcher
import android.widget.EditText
import androidx.core.widget.doAfterTextChanged

fun EditText.setupForCurrencyInput(
    onChanged: (String) -> Unit,
): TextWatcher {
    return doAfterTextChanged { editable ->
        val initialText = editable.toString()
        val checkedText = initialText
            .let {
                val firstChar = it.firstOrNull() ?: return@let it
                if (firstChar != '.' && firstChar != ',') return@let it
                "0$it"
            }
            .let {
                if (it.firstOrNull() != '0') return@let it
                if (it.length <= 1) return@let it
                if (it.getOrNull(1)?.isDigit() == false) return@let it
                it.substring(1, it.length)
            }

        if (initialText != checkedText) {
            setText(checkedText)
            setSelection(length())
        } else {
            onChanged.invoke(initialText)
        }
    }
}