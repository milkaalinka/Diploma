package com.alinaincorporated.diploma.ui.transactions

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.alinaincorporated.diploma.R
import com.alinaincorporated.diploma.databinding.FragmentTransactionsBinding

class TransactionsFragment : Fragment(R.layout.fragment_transactions) {

    private val binding: FragmentTransactionsBinding by viewBinding(
        FragmentTransactionsBinding::bind
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        binding.textBalance.text = getString(R.string.title_balance, "$0.00")
    }

    private fun initViews() = with(binding) {

        buttonAdd.setOnClickListener {
            openAddTransactionsScreen()
            recyclerViewTransactions.apply {
            }
        }
    }

    private fun openAddTransactionsScreen() {
        findNavController().navigate(
            TransactionsFragmentDirections.actionOpenAddTransaction()
        )
    }
}