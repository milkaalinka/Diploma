package com.alinaincorporated.diploma.ui.add_transaction

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.alinaincorporated.diploma.R
import com.alinaincorporated.diploma.database.TransactionDao
import com.alinaincorporated.diploma.database.TransactionEntity
import com.alinaincorporated.diploma.databinding.FragmentAddTransactionBinding
import com.alinaincorporated.diploma.ui.transactions.TransactionCategoryMapper
import com.alinaincorporated.diploma.ui.utils.input_filter.CurrencyInputFilter
import com.alinaincorporated.diploma.ui.utils.input_filter.setupForCurrencyInput
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

class AddTransactionFragment : Fragment(R.layout.fragment_add_transaction) {

    companion object {
        private const val DATE_FORMAT = "dd/MM/yyyy HH:mm"

        private const val TAG_DATE_PICKER = "date_picker"
        private const val TAG_TIME_PICKER = "time_picker"
    }

    private val transactionDao: TransactionDao by inject()

    private val binding: FragmentAddTransactionBinding by viewBinding(
        FragmentAddTransactionBinding::bind
    )

    private var categoryMapper: TransactionCategoryMapper? = null

    private var selectedDateTime: Long? = null
    private var amount: String = ""
    private var selectedCategoryId: Int? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categoryMapper = TransactionCategoryMapper(resources)
        initViews()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        categoryMapper = null
    }

    private fun initViews() = with(binding) {
        initTransactionTypePicker()

        amountInput.filters += CurrencyInputFilter()
        amountInput.setupForCurrencyInput {
            amount = it
        }

        dateInput.setOnClickListener {
            openDatePicker()
        }

        initCategoriesPicker()
    }

    private fun initTransactionTypePicker() = with(binding) {
        transactionTypeInput.setOnItemClickListener { _, _, position, _ ->
            val isIncome = position == 0
            initCategoriesViewAdapter(isIncome)
        }
        initTransactionTypeViewAdapter()

        // Set expense by default
        val expenseText = transactionTypeInput.adapter.getItem(1).toString()
        transactionTypeInput.setText(expenseText, false)
    }

    private fun initTransactionTypeViewAdapter() = with(binding) {
        val transactionType = resources.getStringArray(R.array.transactionTypes)
        val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_type_items, transactionType)
        transactionTypeInput.setAdapter(adapter)
    }

    private fun initCategoriesPicker() = with(binding) {
        categoryInput.setOnItemClickListener { _, _, position, _ ->
            val categoryText = transactionTypeInput.adapter.getItem(position) as String
            selectedCategoryId = categoryMapper?.mapToId(categoryText)
        }
        initCategoriesViewAdapter(isIncome = false)
    }

    private fun initCategoriesViewAdapter(isIncome: Boolean) = with(binding) {
        val categoriesArrayRes =
            if (isIncome) R.array.incomeCategories
            else R.array.expenseCategories
        val categories = resources.getStringArray(categoriesArrayRes)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_type_items, categories)
        categoryInput.setAdapter(arrayAdapter)
    }

    private fun openDatePicker() {
        val picker = AddTransactionDatePickerHelper.createPicker()
        picker.addOnPositiveButtonClickListener {
            selectedDateTime = it
            openTimePicker()
        }
        picker.show(childFragmentManager, TAG_DATE_PICKER)
    }

    private fun openTimePicker() {
        val picker = AddTransactionTimePickerHelper.createPicker()
        picker.addOnPositiveButtonClickListener {
            val selectedDate = selectedDateTime ?: return@addOnPositiveButtonClickListener
            val dateTime = AddTransactionTimePickerHelper.withDate(picker, selectedDate)
            selectedDateTime = dateTime

            binding.dateInput.setText(formatDate(dateTime))
        }
        picker.show(childFragmentManager, TAG_TIME_PICKER)
    }

    private fun formatDate(millis: Long): String {
        val format = SimpleDateFormat(DATE_FORMAT, Locale.ROOT).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }
        return format.format(Date(millis))
    }

    private fun test() {
        lifecycleScope.launch(Dispatchers.IO) {
            val transaction = TransactionEntity(
                amount = Random.nextFloat() * 100 + 1,
                description = "",
                dateTime = Clock.System.now(),
                categoryId = Random.nextInt(1, 10),
                isIncome = Random.nextBoolean(),
            )
            transactionDao.insert(transaction)

            val transactions = transactionDao.getAll()

            val transactionsText = transactions.joinToString(separator = "\n") { it.toString() }

            /*withContext(Dispatchers.Main) {
                binding.textTest.text = transactionsText
            */
        }
    }
}

