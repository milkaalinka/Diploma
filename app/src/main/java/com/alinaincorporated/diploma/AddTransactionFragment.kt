package com.alinaincorporated.diploma

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.alinaincorporated.diploma.databinding.FragmentAddTransactionBinding

class AddTransactionFragment : Fragment(R.layout.fragment_add_transaction) {

    private val binding: FragmentAddTransactionBinding by viewBinding(
        FragmentAddTransactionBinding::bind
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}