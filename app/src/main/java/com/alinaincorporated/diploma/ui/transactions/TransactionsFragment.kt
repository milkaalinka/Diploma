package com.alinaincorporated.diploma.ui.transactions

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.alinaincorporated.diploma.R
import com.alinaincorporated.diploma.database.TransactionDao
import com.alinaincorporated.diploma.database.TransactionEntity
import com.alinaincorporated.diploma.databinding.FragmentTransactionsBinding
import com.alinaincorporated.diploma.ui.transactions.adapter.TransactionAdapter
import com.alinaincorporated.diploma.ui.utils.formatMoney
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject

class TransactionsFragment : Fragment(R.layout.fragment_transactions) {

    private val binding: FragmentTransactionsBinding by viewBinding(
        FragmentTransactionsBinding::bind
    )

    private val transactionDao: TransactionDao by inject()

    private var categoryMapper: TransactionCategoryMapper? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categoryMapper = TransactionCategoryMapper(resources)
        initViews()
        loadTransactions()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        categoryMapper = null
    }

    private fun initViews() = with(binding) {
        recyclerTransactions.layoutManager = LinearLayoutManager(requireContext())
        recyclerTransactions.addItemDecoration(
            DividerItemDecoration(requireContext(), RecyclerView.VERTICAL)
        )

        buttonAdd.setOnClickListener {
            openAddTransactionsScreen()
        }
    }

    private fun loadTransactions() {
        lifecycleScope.launch(Dispatchers.IO) {
            val transactions = transactionDao.getAll()
            val balance = reduceBalance(transactions)

            withContext(Dispatchers.Main) {
                val mapper = categoryMapper ?: return@withContext
                binding.recyclerTransactions.adapter = TransactionAdapter(
                    transactions = transactions,
                    categoryMapper = mapper,
                )

                setBalance(balance)
            }
        }
    }

    private fun reduceBalance(transactions: List<TransactionEntity>): Float {
        var balance = 0f
        transactions.forEach { transaction ->
            balance += transaction.absAmount
        }
        return balance
    }

    private fun setBalance(balance: Float) {
        val formattedBalance = formatMoney(balance)
        binding.textBalance.text = getString(R.string.title_balance, formattedBalance)
    }

    private fun openAddTransactionsScreen() {
        findNavController().navigate(
            TransactionsFragmentDirections.actionOpenAddTransaction()
        )
    }
}