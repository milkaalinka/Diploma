package com.alinaincorporated.diploma

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.alinaincorporated.diploma.databinding.FragmentStatisticBinding

class StatisticFragment : Fragment(R.layout.fragment_statistic) {

    private val binding: FragmentStatisticBinding by viewBinding(
        FragmentStatisticBinding::bind
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)
    }

}