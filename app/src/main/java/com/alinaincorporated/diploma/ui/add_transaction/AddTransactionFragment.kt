package com.alinaincorporated.diploma.ui.add_transaction

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
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
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat
import java.util.*

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

    private var isExpense: Boolean = true
    private var selectedDateTime: Long? = null
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
            amountLayout.error = null
        }

        dateInput.setOnClickListener {
            openDatePicker()
        }
        dateInput.doAfterTextChanged {
            dateLayout.error = null
        }

        initCategoriesPicker()
        categoryInput.doAfterTextChanged {
            categoryLayout.error = null
        }

        saveButton.setOnClickListener {
            validateAndSave()
        }
    }

    private fun initTransactionTypePicker() = with(binding) {
        transactionTypeInput.setOnItemClickListener { _, _, position, _ ->
            val isExpense = position == 1
            this@AddTransactionFragment.isExpense = isExpense
            initCategoriesViewAdapter(isExpense)
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
            val categoryText = categoryInput.adapter.getItem(position) as String
            selectedCategoryId = categoryMapper?.mapToId(categoryText)
        }
        initCategoriesViewAdapter(isExpense = false)
    }

    private fun initCategoriesViewAdapter(isExpense: Boolean) = with(binding) {
        val categoriesArrayRes =
            if (isExpense) R.array.expenseCategories
            else R.array.incomeCategories
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

    private fun validateAndSave() {
        val amountRaw = binding.amountInput.text.toString()
        val description = binding.descriptionInput.text.toString()

        var isValid = true

        val dateTime = selectedDateTime
        if (dateTime == null) {
            isValid = false
            binding.dateLayout.error = getString(R.string.error_add_transaction_empty_date_time)
        }

        val amount = amountRaw.toFloatOrNull()
        if (amount == null) {
            isValid = false
            binding.amountLayout.error = getString(R.string.error_add_transaction_invalid_amount)
        }
        if (amount != null && amount < 0.01) {
            isValid = false
            binding.amountLayout.error = getString(R.string.error_add_transaction_amount_zero)
        }

        val categoryId = selectedCategoryId
        if (categoryId == null) {
            isValid = false
            binding.categoryLayout.error = getString(R.string.error_add_transaction_empty_category)
        }

        if (!isValid) return

        save(
            isExpense = isExpense,
            dateTime = dateTime!!,
            amount = amount!!,
            description = description.trim().takeIf { it.isNotEmpty() },
            categoryId = categoryId!!,
        )
    }

    private fun save(
        isExpense: Boolean,
        dateTime: Long,
        amount: Float,
        description: String?,
        categoryId: Int,
    ) {
        lifecycleScope.launch(Dispatchers.IO) {
            val transaction = TransactionEntity(
                amount = amount,
                description = description,
                dateTime = dateTime,
                categoryId = categoryId,
                isExpense = isExpense,
            )
            transactionDao.insert(transaction)

            withContext(Dispatchers.Main) {
                findNavController().navigateUp()
            }
        }
    }
}
