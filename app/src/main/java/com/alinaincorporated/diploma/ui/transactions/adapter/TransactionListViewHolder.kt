package com.alinaincorporated.diploma.ui.transactions.adapter

import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.alinaincorporated.diploma.R
import com.alinaincorporated.diploma.database.TransactionEntity
import com.alinaincorporated.diploma.databinding.ListItemViewBinding
import com.alinaincorporated.diploma.ui.transactions.TransactionCategoryMapper
import com.alinaincorporated.diploma.ui.utils.formatDateTime
import com.alinaincorporated.diploma.ui.utils.formatMoney
import com.alinaincorporated.diploma.ui.utils.inflater

class TransactionListViewHolder private constructor(
    private val binding: ListItemViewBinding,
    private val categoryMapper: TransactionCategoryMapper,
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun create(
            parent: ViewGroup,
            categoryMapper: TransactionCategoryMapper,
        ) = TransactionListViewHolder(
            binding = ListItemViewBinding.inflate(parent.inflater, parent, false),
            categoryMapper = categoryMapper,
        )
    }

    fun bind(item: TransactionEntity) = with(binding) {
        transactTextName.text = categoryMapper.mapFromId(item.categoryId)
        transactDescription.isVisible = !item.description.isNullOrEmpty()
        transactDescription.text = item.description
        transactDate.text = item.formattedDateTime()
        transactAmount.text = item.formattedAmount()
        transactAmount.setTextColor(item.amountColor())
    }

    private fun TransactionEntity.formattedDateTime(): String {
        return formatDateTime(dateTime)
    }

    private fun TransactionEntity.formattedAmount(): String {
        return formatMoney(absAmount)
    }

    private fun TransactionEntity.amountColor(): Int {
        val colorRes =
            if (isExpense) R.color.expense
            else R.color.income

        return ContextCompat.getColor(itemView.context, colorRes)
    }
}