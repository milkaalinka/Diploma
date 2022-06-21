package com.alinaincorporated.diploma.ui.statistics

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.alinaincorporated.diploma.R
import com.alinaincorporated.diploma.database.TransactionDao
import com.alinaincorporated.diploma.database.TransactionEntity
import com.alinaincorporated.diploma.databinding.FragmentStatisticsBinding
import com.alinaincorporated.diploma.ui.transactions.TransactionCategoryMapper
import com.alinaincorporated.diploma.ui.utils.formatMoney
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
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
        initViews()
        loadData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        categoryMapper = null
    }

    private fun initViews() = with(binding) {
        chartIncome.initChart(R.string.title_statistics_chart_income)
        chartExpense.initChart(R.string.title_statistics_chart_expense)
    }

    private fun PieChart.initChart(@StringRes titleRes: Int) {
        description.text = ""
        centerText = getString(titleRes)
        setCenterTextSize(18f)
        setCenterTextColor(ContextCompat.getColor(requireContext(), R.color.orange_700))
        setCenterTextTypeface(Typeface.DEFAULT_BOLD)
        setEntryLabelColor(Color.WHITE)
    }

    private fun loadData() {
        lifecycleScope.launch(Dispatchers.IO) {
            val transactions = transactionDao.getAll()

            loadTotals(transactions)
            loadCategoriesChart(transactions)
        }
    }

    private suspend fun loadTotals(transactions: List<TransactionEntity>) {
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

    private suspend fun loadCategoriesChart(transactions: List<TransactionEntity>) {
        val incomeCategoriesAmounts: MutableMap<Int, Float> = HashMap()
        val expenseCategoriesAmounts: MutableMap<Int, Float> = HashMap()

        transactions.forEach { transaction ->
            val targetMap =
                if (transaction.isExpense) expenseCategoriesAmounts
                else incomeCategoriesAmounts

            val current = targetMap.getOrPut(transaction.categoryId) { 0f }
            targetMap[transaction.categoryId] = current + transaction.amount
        }

        // Convert to percents
        incomeCategoriesAmounts.normalize()
        expenseCategoriesAmounts.normalize()

        withContext(Dispatchers.Main) {
            binding.chartIncome.submitCategories(
                categories = incomeCategoriesAmounts,
                titleRes = R.string.title_statistics_chart_income,
                colors = StatisticsColors.income,
            )
            binding.chartExpense.submitCategories(
                categories = expenseCategoriesAmounts,
                titleRes = R.string.title_statistics_chart_expense,
                colors = StatisticsColors.expense,
            )
        }
    }

    private fun MutableMap<Int, Float>.normalize() {
        val sum = values.sum()
        forEach { (id, amount) ->
            this[id] = amount / sum * 100
        }
    }

    private fun PieChart.submitCategories(
        categories: Map<Int, Float>,
        @StringRes titleRes: Int,
        colors: IntArray,
    ) {
        val mapper = categoryMapper ?: return

        val entries = categories.map { (id, amount) ->
            PieEntry(
                amount,
                mapper.mapFromId(id)
            )
        }

        val dataSet = PieDataSet(entries, getString(titleRes))
        dataSet.setColors(*colors)
        dataSet.valueTextColor = Color.WHITE
        dataSet.valueTextSize = 12f
        val data = PieData(dataSet)
        setData(data)
        invalidate()
    }
}