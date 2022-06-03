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
import com.alinaincorporated.diploma.ui.utils.input_filter.CurrencyInputFilter
import com.alinaincorporated.diploma.ui.utils.input_filter.setupForCurrencyInput
import com.google.android.material.datepicker.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

class AddTransactionFragment : Fragment(R.layout.fragment_add_transaction) {

    companion object {
        private const val DATE_FORMAT = "dd/MM/yyyy"

        private const val TAG_DATE_PICKER = "date_picker"
    }

    private val transactionDao: TransactionDao by inject()

    private val binding: FragmentAddTransactionBinding by viewBinding(
        FragmentAddTransactionBinding::bind
    )

    private var selectedDate: Long? = null
    private var amount: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
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
        initCategoriesViewAdapter(isIncome = false)
    }

    private fun initTransactionTypeViewAdapter() = with(binding) {
        val transactionType = resources.getStringArray(R.array.transactionTypes)
        val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_type_items, transactionType)
        transactionTypeInput.setAdapter(adapter)

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
        val picker = MaterialDatePicker.Builder.datePicker()
            .setTitleText(R.string.title_add_transaction_date_picker)
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .setCalendarConstraints(createDatePickerConstraints())
            .build()

        picker.addOnPositiveButtonClickListener {
            selectedDate = it
            binding.dateInput.setText(formatDate(it))
        }

        picker.show(childFragmentManager, TAG_DATE_PICKER)
    }

    private fun createDatePickerConstraints(): CalendarConstraints {
        val today = MaterialDatePicker.todayInUtcMilliseconds()
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        calendar.timeInMillis = today
        setCalendarAtStartOfDay(calendar)
        val max = calendar.timeInMillis

        calendar.add(Calendar.MONTH, -1)
        val min = calendar.timeInMillis

        val validator = CompositeDateValidator.allOf(
            listOf(
                DateValidatorPointForward.from(min),
                DateValidatorPointBackward.before(max),
            )
        )

        return CalendarConstraints.Builder()
            .setStart(min)
            .setEnd(max)
            .setValidator(validator)
            .build()
    }

    private fun setCalendarAtStartOfDay(calendar: Calendar) {
        calendar[Calendar.MILLISECOND] = 0
        calendar[Calendar.SECOND] = 0
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.HOUR] = 0
    }

    private fun formatDate(millis: Long): String {
        val date = Date(millis)
        return SimpleDateFormat(DATE_FORMAT, Locale.ROOT).format(date)
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

