package com.alinaincorporated.diploma.ui.statistics

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.alinaincorporated.diploma.R
import com.alinaincorporated.diploma.database.TransactionDao
import com.alinaincorporated.diploma.databinding.FragmentStatisticsBinding
import com.alinaincorporated.diploma.ui.transactions.TransactionCategoryMapper
import com.alinaincorporated.diploma.ui.utils.formatMoney
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject

class StatisticsFragment : Fragment(R.layout.fragment_statistics) {

    private val binding: FragmentStatisticsBinding by viewBinding(
        FragmentStatisticsBinding::bind
    )

    private val transactionDao: TransactionDao by inject()

    private var categoryMapper: TransactionCategoryMapper? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)
        categoryMapper = TransactionCategoryMapper(resources)
        loadData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        categoryMapper = null
    }

    private fun loadData() {
        lifecycleScope.launch(Dispatchers.IO) {
            val transactions = transactionDao.getAll()

            var income = 0f
            var expense = 0f

            transactions.forEach { transaction ->
                if (transaction.isExpense) expense += transaction.amount
                else income += transaction.amount
            }

            val total = income - expense

            withContext(Dispatchers.Main) {
                binding.textIncome.text = formatMoney(income)
                binding.textExpense.text = formatMoney(expense)
                binding.textTotal.text = formatMoney(total)
            }
        }
    }
}