package com.alinaincorporated.diploma.ui.utils.input_filter

class CurrencyInputFilter : ComplexInputFilter(
    listOf(
        OnlyOneZeroAtStartInputFilter(),
        DecimalDigitsInputFilter()
    )
)