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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import org.koin.android.ext.android.inject
import kotlin.random.Random

class AddTransactionFragment : Fragment(R.layout.fragment_add_transaction) {

    private val transactionDao: TransactionDao by inject()

    private val binding: FragmentAddTransactionBinding by viewBinding(
        FragmentAddTransactionBinding::bind
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //test()

        val transactionType = resources.getStringArray(R.array.transactionTypes)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_type_items, transactionType)
        binding.autoCompleteTextViewTransactionType.setAdapter(arrayAdapter)

        val incomeCategories = resources.getStringArray(R.array.incomeCategories)
        val  arrayAdapterIncome = ArrayAdapter(requireContext(),R.layout.dropdown_type_items, incomeCategories)
        binding.textViewCategories.setAdapter(arrayAdapterIncome)

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

